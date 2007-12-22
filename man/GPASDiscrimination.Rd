\name{GPASDiscrimination}
\alias{GPASDiscrimination}
\title{ Execute the GPAS algorithm for discrimination }
\description{
 Working on categorical data with binary response, the algorithm searches for multi-valued logic expressions in disjunctive normal form discriminating between response 0 and response 1. 
 The algorithm is intended for genetic association studies on SNP data. 
}
\usage{
GPASDiscrimination(resp.train, preds.train, resp.test=NULL,
  preds.test=NULL, runs = 1, generations = 10000)
}
\arguments{
  \item{resp.train}{Vector with the response variables of the training data set}
  \item{preds.train}{Matrix or data frame with the predictors of the training data set}
  \item{resp.test}{Optional vector with the response variables of the test data set}
  \item{preds.test}{Optional matrix or data frame with the predictors of the test data set}
  \item{runs}{Number of independent runs of GPAS}
  \item{generations}{Number of generations after which the algorithm will be stopped}
}
\value{
  Returns an object of class \code{GPAS} with a \code{data.frame} in its slot \code{summary} containing information about the last population of the executed discrimination runs. 
  For each individual in the last population the following information is contained:
  \item{data set}{Either 'training' or 'test' or omitted}
  \item{run}{The run the individual was found in}
  \item{generation}{The generation the individual was created in}
  \item{objective value 1}{Correctly predicted cases}
  \item{objective value 2}{Correctly predicted controls}
  \item{objective value 3}{Length of the individual}
  \item{individual}{A string representation of the individual}
}
\references{R. Nunkesser, T. Bernholt, H. Schwender, K. Ickstadt, and I. Wegener (2007). Detecting High-Order Interactions of Single Nucleotide Polymorphisms Using Genetic Programming. \emph{Bioinformatics}, \bold{23}, 3280-3288. }
\author{Robin Nunkesser \email{Robin.Nunkesser@tu-dortmund.de}}
\seealso{\code{"\linkS4class{GPAS}"}, \code{\link{GPASInteractions}}}
\examples{
# load example data
data(data.logicfs)

# execute GPAS to discriminate between cases and controls
GPASDiscrimination(cl.logicfs,data.logicfs,runs=1,generations=1000)
}
\keyword{tree}
\keyword{interface}
