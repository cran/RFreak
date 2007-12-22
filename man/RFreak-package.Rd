\name{RFreak-package}
\alias{RFreak-package}
\alias{RFreak}
\docType{package}
\title{
R/FrEAK interface
}
\description{
An R interface to a modified version of the Free
Evolutionary Algorithm Kit FrEAK. FrEAK is a toolkit written in
Java to design and analyze evolutionary algorithms. Both the R
interface an extended version of FrEAK are contained in the
RFreak package. For more information on FrEAK see
http://sourceforge.net/projects/freak427/.
}
\details{
\tabular{ll}{
Package: \tab RFreak\cr
Version: \tab 0.2-0\cr
Date: \tab 2007-12-22\cr
Depends: \tab R (>= 2.6.0), rJava (>= 0.5.0)\cr
LazyLoad: \tab yes\cr
License: \tab GPL (>= 2)\cr
SystemRequirements: \tab Java (>= 5.0)\cr
}

Index:
\preformatted{
FreakReturn-class       Class "FreakReturn"
GPAS-class              Class "GPAS"
GPASDiscrimination      Execute the GPAS algorithm for discrimination
GPASInteractions        Execute the GPAS algorithm for feature selection
LTSevol                 Least Trimmed Squares Robust Regression
data.logicfs            Example Data of GPAS
executeSchedule         Executes a FrEAK schedule
launchScheduleEditor    Launches a graphical schedule editor
ltsEA-class             Class "ltsEA"
predict-method          predict using new predictors
}
}
\author{
Robin Nunkesser <Robin.Nunkesser@tu.dortmund.de>
}
\references{
FrEAK: \url{http://sourceforge.net/projects/freak427/}

R. Nunkesser, T. Bernholt, H. Schwender, K. Ickstadt, and I. Wegener (2007). Detecting High-Order Interactions of Single Nucleotide Polymorphisms Using Genetic Programming. \emph{Bioinformatics}, \bold{23}, 3280-3288.

}
\keyword{ package }
