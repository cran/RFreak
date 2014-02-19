\name{GPASInteractions}
\alias{GPASInteractions}
\title{Execute the GPAS algorithm for feature selection}
\description{
Identification of interesting (high order) SNP interactions. The algorithm works on categorical data with binary response 
and delivers multi-valued logic expressions in disjunctive normal form typically explaining subsets of the data and an interaction tree containing interesting interactions.
}
\usage{
GPASInteractions(resp, preds, runs = 1, generations = 10000, 
  savegraph = "interactions.dot",occurences=10,ratio=0.1)
}
\arguments{
  \item{resp}{Vector with the response variables}
  \item{preds}{Matrix or data frame with the predictors}
  \item{runs}{Number of independent runs of GPAS}
  \item{generations}{Number of generations after which the algorithm will be stopped}
  \item{savegraph}{Name of the file the resulting GraphViz graph will be saved to}
  \item{occurences}{The minimum number of times an interaction has to occur to be included in the graph}
  \item{ratio}{The minimal ratio a single literal has to occur in relation to his ancestor in the interaction graph to be included in the interaction graph}  
}
\value{
  Returns an object of class \code{GPAS} with a \code{data.frame} in its slot \code{summary}  containing information about the last population of the executed runs. 
  For each individual in the last population the following information is contained:
  \item{run}{The run the individual was found in}
  \item{generation}{The generation the individual was created in}
  \item{objective value 1}{Sum of correctly predicted cases and controls}
  \item{objective value 2}{Correctly predicted controls}
  \item{objective value 3}{Length of the individual}
  \item{individual}{A string representation of the individual}
}
\references{R. Nunkesser, T. Bernholt, H. Schwender, K. Ickstadt, and I. Wegener (2007). Detecting High-Order Interactions of Single Nucleotide Polymorphisms Using Genetic Programming. \emph{Bioinformatics}, \bold{23}, 3280-3288. 

GraphViz: \url{http://www.graphviz.org/}
}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{\code{"\linkS4class{GPAS}"}, \code{\link{GPASDiscrimination}}}
\examples{
# load example data
data(data.logicfs)

# execute GPAS to search for interesting interactions
GPASInteractions(cl.logicfs,data.logicfs,runs=1,generations=1000)
}
\keyword{tree}
\keyword{interface}