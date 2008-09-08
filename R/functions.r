.tempInit<-function() {
	require(rJava)	
	if (.Platform$OS.type=="windows") {	
		.jinit(	
				c(
					"C:/sfb475a5/FrEAK",
					"C:/sfb475a5/FrEAK/export/lib/bsh-2.0b1.jar",
					"C:/sfb475a5/FrEAK/export/lib/colt.jar",
					"C:/sfb475a5/FrEAK/export/lib/jargs.jar",
					"C:/sfb475a5/FrEAK/export/lib/jdom.jar",
					"C:/sfb475a5/FrEAK/export/lib/jgraph.jar",
					"C:/sfb475a5/FrEAK/export/lib/jicos-system.jar",
					"C:/sfb475a5/FrEAK/export/lib/jlfgr-1_0.jar",
					"C:/sfb475a5/FrEAK/export/lib/ostermillerutils_1_04_03_for_kaffe.jar"
				),parameters=c("-Xms1G","-Xmx1G")			
			);
	 } else {
		.jinit(
				c("/Users/nunkesser/sfb475a5/RFrEAK/bin",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/jargs.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/jdom.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/bsh-2.0b1.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/jgraph.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/colt.jar",		
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/jicos-system.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/jlfgr-1_0.jar",
				 "/Users/nunkesser/sfb475a5/RFrEAK/lib/ostermillerutils_1_04_03_for_kaffe.jar"
				),parameters=c("-Xms1G","-Xmx1G","-Djava.awt.headless=true")
			);
	 }

#		.jinit(
#				c("/Users/nunkesser/sfb475a5/RFreakPackage/inst/java/rfreak-0.2.jar"),
#				parameters=c("-Xms1G","-Xmx1G","-Djava.awt.headless=true")
#			);

	#.jcall("freak/Freak","V","setDebugLevel",as.integer(5));

	.jcall("freak/rinterface/control/LogRegInterface", "V", "setRMode");   
	.jcall("freak/rinterface/control/RFlags", "V", "setUseCase",.jfield("freak/rinterface/control/RFlags","I","R"));   
	
}

setClass("FreakReturn", representation(summary="data.frame"))

setMethod("show","FreakReturn",
	function(object) {
		cat("Result obtained from FrEAK:\n")
		show(object@summary)
	}
)

setClass("evolreg", representation("FreakReturn",best="numeric",coefficients="numeric",crit="numeric"))

setMethod("show","evolreg",
		function(object) {
			cat("\nResult obtained from FrEAK:\n")
			show(object@summary)
			cat("\nChosen subset:\n")
			show(object@best)
			cat("\nCoefficients:\n")
			show(object@coefficients)
			cat("\nCriterion:\n")		
			show(object@crit)
		}
		)

setClass("ltsEA", representation("evolreg"))

setMethod("show","ltsEA",
	function(object) {
		cat("\nResult obtained from FrEAK:\n")
		show(object@summary)
		cat("\nChosen subset:\n")
		show(object@best)
		cat("\nCoefficients:\n")
		show(object@coefficients)
		cat("\nCriterion:\n")		
		show(object@crit)
	}
)

setClass("GPAS", representation("FreakReturn",trees="list"))

setGeneric("predict",useAsDefault=getExportedValue("stats","predict"))
setMethod("predict","GPAS",
	function(object,individual,preds) {
		resp<-rep(0,dim(preds)[1])
	   	.checkData(resp,preds)
		preds <- cbind(resp,preds); 
	    preds = matrix(as.integer(preds), dim(preds)[1], dim(preds)[2]);    	
		columnNames<-as.character("FK")
		for (i in 1:(dim(preds)[[2]]-1)) {
			columnNames<-c(columnNames,paste("SNP",as.character(i),sep=""))
		} 				
		trainingDataObject <- .jnew("freak/module/searchspace/logictree/RData",.jarray(preds),.jarray(dim(preds)),.jarray(columnNames));
		.jcall("freak/module/searchspace/logictree/Data", "V", "setTrainingData", trainingDataObject);	
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRData", trainingDataObject);
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRMode");	   		
		.jcall(object@trees[[individual]],"V", "update")	   	
		bitset<-.jcall(object@trees[[individual]],"Ljava/util/BitSet;", "getCharacteristicBitSet")
		return(.jcall(object@trees[[individual]],"[I", "getCharacteristicIntSet"))
		
	}
)


.extractAtomicVector<-function(jSAtomicVector) {
	return(.jcall(jSAtomicVector,.jcall(jSAtomicVector,"S","getJNISignature"),"getValues"));
}

.extractList<-function(jSList) {
	jVector <- .jcall(jSList,"[Lfreak/rinterface/model/SAbstractAtomicVector;","getValues");
	returnList<-list()
	for (i in 1:length(jVector)) {
	 returnList[i]<-list(.extractAtomicVector(jVector[[i]]));
	}
	return(returnList)
}

.extractDataFrame<-function(jSDataFrame) {
	values<-.extractList(jSDataFrame)
	returnFrame<-data.frame(values)
	colnames(returnFrame)<-.jcall(jSDataFrame,"[S","getColnames");
	return(returnFrame)
}

.checkData <- function(resp, preds,allowNegative=FALSE){ 
	preds <- as.matrix(preds)
	n2 <- length(preds)/length(resp)           
	if(length(resp)!= length(preds[,1])) stop("length of response must be equal to number of data rows")
	if (sum(is.na(preds))!=FALSE) stop("predictors contain NA")	    
	if (n2 != floor(n2)) stop("length of data not a multiple of response")
	if (n2 < 1) stop("data mandatory")
   	if ((!allowNegative) && (sum(preds<0) != FALSE)) stop("some negative data among the predictors")        
}

robreg.evol <- function(x,y,method=c("lts","lta","lms","lqs","lqd"), quantile=NULL,adjust=FALSE,runs=1,generations=10000,duration=0){
		method<-match.arg(method)
		.checkData(y,x,allowNegative=TRUE)
		.jcall("freak/module/searchspace/PointSet","V","setPointsSetFromR",as.logical(TRUE));		
		y<-as.matrix(y)
		x<-as.matrix(x)
		matrix<-cbind(y,x)
		data <- .jarray(matrix);
		dataDim <- .jarray(dim(matrix));
		dataObject <- .jnew("freak/rinterface/model/RDoubleMatrix",data,dataDim);	
		if (is.null(quantile)) quantile=floor((sum(dim(matrix))+1)/2.0)
		schedule<-.jcall("freak/rinterface/model/ScheduleConfigurator","Lfreak/core/control/Schedule;","getLTSSchedule",dataObject,as.integer(quantile),as.logical(adjust),as.integer(runs),as.integer(generations),as.integer(duration),method);
		.jcall("freak/rinterface/control/LogRegInterface","V","rSetSchedule",schedule);
		cmdargs = .jarray(c(""));
		.jcall("freak/rinterface/control/RFreak","V", "rMain", cmdargs);
		returnedFrame<-	.extractDataFrame(.jcall("freak/rinterface/model/RReturns", "Lfreak/rinterface/model/SDataFrame;", "getDataFrame"))
		crit<-.jcall("freak/rinterface/model/RReturns", "D", "getResidual");
		coefficients<-.jcall("freak/rinterface/model/RReturns", "[D", "getFittedHyperplane");
		best<-.jcall("freak/rinterface/model/RReturns", "[I", "getChosenIndices")+1;
		.jcall("freak/module/searchspace/PointSet","V","setPointsSetFromR",as.logical(TRUE));			
		return(new("evolreg",summary=returnedFrame,best=best,coefficients=coefficients,crit=crit))	
}

ltsreg.evol <- function(...)
{
	oc <- sys.call()
	oc$method <- "lts"
	oc[[1]] <- quote(RFreak::robreg.evol)
	eval.parent(oc)
}

ltareg.evol <- function(...)
{
	oc <- sys.call()
	oc$method <- "lta"
	oc[[1]] <- quote(RFreak::robreg.evol)
	eval.parent(oc)
}

lmsreg.evol <- function(...)
{
	oc <- sys.call()
	oc$method <- "lms"
	oc[[1]] <- quote(RFreak::robreg.evol)
	eval.parent(oc)
}

lqsreg.evol <- function(...)
{
	oc <- sys.call()
	oc$method <- "lqs"
	oc[[1]] <- quote(RFreak::robreg.evol)
	eval.parent(oc)
}

lqdreg.evol <- function(...)
{
	oc <- sys.call()
	oc$method <- "lqd"
	oc[[1]] <- quote(RFreak::robreg.evol)
	eval.parent(oc)
}

LTSevol <- function(y,x,h=NULL,adjust=FALSE,runs=1,generations=10000){
	return(robreg.evol(x,y,"lts",h,adjust,runs,generations,0))
}

GPASInteractions <- function(resp,preds,runs=1,generations=10000,savegraph = "interactions.dot",occurences=10,ratio=0.1){
		.checkData(resp,preds)
		preds <- cbind(resp,preds); 
	    preds = matrix(as.integer(preds), dim(preds)[1], dim(preds)[2]);    	
		columnNames<-as.character("FK")
		for (i in 1:(dim(preds)[[2]]-1)) {
			columnNames<-c(columnNames,paste("SNP",as.character(i),sep=""))
		} 				
		trainingDataObject <- .jnew("freak/module/searchspace/logictree/RData",.jarray(preds),.jarray(dim(preds)),.jarray(columnNames));
		.jcall("freak/module/searchspace/logictree/Data", "V", "setTrainingData", trainingDataObject);	
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRData", trainingDataObject);
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRMode");	   		
		.jcall("freak/rinterface/model/ScheduleConfigurator","V","setInteractionR",as.integer(runs),as.integer(generations),as.character(savegraph),as.integer(occurences),ratio);		
		schedule <- .jcall("freak/rinterface/model/ScheduleConfigurator","Lfreak/core/control/Schedule;","getCurrentSchedule");
		.jcall("freak/rinterface/control/LogRegInterface","V","rSetSchedule",schedule);
		cmdargs = .jarray(c(""));
		.jcall("freak/rinterface/control/RFreak","V", "rMain", cmdargs);
		returnedFrame<-	.extractDataFrame(.jcall("freak/rinterface/model/RReturns", "Lfreak/rinterface/model/SDataFrame;", "getDataFrame"))	
		returnedTrees<-.jcall("freak/rinterface/model/RReturns", "[Lfreak/module/searchspace/logictree/DNFTree;", "getAllTrees")
		.jcall("freak/module/searchspace/logictree/Data", "V", "clear");		
		return(new("GPAS",summary=returnedFrame,trees=returnedTrees))	
}

GPASDiscrimination <- function(resp.train,preds.train,resp.test=NULL, preds.test=NULL, runs=1,generations=10000){
		.checkData(resp.train,preds.train)
		preds.train <- cbind(resp.train,preds.train); 
		preds.train = matrix(as.integer(preds.train), dim(preds.train)[1], dim(preds.train)[2]);
		columnNames<-as.character("FK")
		for (i in 1:(dim(preds.train)[[2]]-1)) {
			columnNames<-c(columnNames,paste("SNP",as.character(i),sep=""))
		} 
		trainingDataObject <- .jnew("freak/module/searchspace/logictree/RData",.jarray(preds.train),.jarray(dim(preds.train)),.jarray(columnNames));
		.jcall("freak/module/searchspace/logictree/Data", "V", "setTrainingData", trainingDataObject);	
		if (!(is.null(resp.test)||is.null(preds.test))) {
			.checkData(resp.test,preds.test)	
		    preds.test <- cbind(resp.test,preds.test);             
			preds.test = matrix(as.integer(preds.test), dim(preds.test)[1], dim(preds.test)[2]);		
			columnNames1<-as.character("FK")
			for (i in 1:(dim(preds.test)[[2]]-1)) {
				columnNames1<-c(columnNames1,paste("SNP",as.character(i),sep=""))
			} 			
			testDataObject <- .jnew("freak/module/searchspace/logictree/RData",.jarray(preds.test),.jarray(dim(preds.test)),.jarray(columnNames1));				
			.jcall("freak/module/searchspace/logictree/Data", "V", "setTestData", testDataObject);					
		}		
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRData", trainingDataObject);	
		.jcall("freak/module/searchspace/logictree/Data", "V", "setRMode");	 				
		.jcall("freak/rinterface/model/ScheduleConfigurator","V","setDiscriminationR",as.integer(runs),as.integer(generations));
		schedule <- .jcall("freak/rinterface/model/ScheduleConfigurator","Lfreak/core/control/Schedule;","getCurrentSchedule");
		.jcall("freak/rinterface/control/LogRegInterface","V","rSetSchedule",schedule);
		cmdargs = .jarray(c(""));
		.jcall("freak/rinterface/control/RFreak","V", "rMain", cmdargs);
		returnedFrame<-	.extractDataFrame(.jcall("freak/rinterface/model/RReturns", "Lfreak/rinterface/model/SDataFrame;", "getDataFrame"))
		returnedTrees<-.jcall("freak/rinterface/model/RReturns", "[Lfreak/module/searchspace/logictree/DNFTree;", "getAllTrees")
		.jcall("freak/module/searchspace/logictree/Data", "V", "clear");		
		return(new("GPAS",summary=returnedFrame,trees=returnedTrees))	
}	

launchScheduleEditor <- function(saveTo="schedule.freak",load=NULL){
		if ((!is.null(Sys.info())) && (Sys.info()[1]=="Darwin")) {
			if (is.null(load)) load<-"NULL"
			system(paste("java -jar ",system.file("java", "rfreak-0.2-4.jar", package = "RFreak")," --edit-schedule='",load,"' --save-edited-schedule='",saveTo,"'",sep=""))
		} else {
			.jcall("freak/gui/scheduleeditor/ScheduleEditor", "V", "setRSaveTo",as.character(saveTo));
			if (is.null(load)) {
			 	.jcall("freak/rinterface/control/RFreak", "V", "showScheduleEditor");	
			 } else {
			 	.jcall("freak/rinterface/control/RFreak", "V", "showScheduleEditor",as.character(load));		 
			 }
		}
}

executeSchedule <- function(freakfile="schedule.freak"){
		.jcall("freak/rinterface/control/LogRegInterface", "V", "setScheduleWillBeSetByR",as.logical(FALSE));  
		cmdargs = .jarray(c(freakfile));
		.jcall("freak/rinterface/control/RFreak","V", "rMain", cmdargs);
		returnedFrame<-	.extractDataFrame(.jcall("freak/rinterface/model/RReturns", "Lfreak/rinterface/model/SDataFrame;", "getDataFrame"))
		return(new("FreakReturn",summary=returnedFrame))	
}

.testInt<-function() {
	set.seed(42);
	resp = sample(rep(0:1,e=5));
	bin = matrix(sample(rep(0:2,e=34))[1:100],10,10);
	runs = 1;
	generations = 1000;
	ret<-GPASInteractions(resp,bin,runs,generations,"test.dot")
	print(ret)
	return(ret)	
}
	
.testDis<-function() {
	set.seed(42);
	resp = sample(rep(0:1,e=5));
	bin = matrix(sample(rep(0:2,e=34))[1:100],10,10);
	resp1 = sample(rep(0:1,e=5));
	bin1 = matrix(sample(rep(0:2,e=34))[1:100],10,10);
	runs = 1;
	generations = 10000;
	ret<-GPASDiscrimination(resp,bin,NULL,NULL,runs,generations)
	print(ret)
	return(ret)
}

.testLTS<-function() {
	bin <- as.matrix(stackloss[, 1:3])
	resp <- as.matrix(stackloss[, 4])
	runs = 1;
	generations = 10000;
	ret<-LTSevol(resp,bin,NULL,TRUE,runs,generations)
	print(ret)
	return(ret)
}

#.tempInit()
#.testInt()