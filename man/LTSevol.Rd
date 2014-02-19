\name{LTSevol}
\alias{LTSevol}
\title{Least Trimmed Squares Robust Regression}
\description{
Carries out least trimmed squares (LTS) robust regression with an evolutionary algorithm.  The LTS regression method minimizes the sum of the \eqn{h} smallest squared residuals. Deprecated. Use \code{\link{robreg.evol}} instead.
}
\usage{
## Deprecated:
LTSevol(y, x, h = NULL, adjust = FALSE, runs = 1, generations = 10000)
}
\arguments{
  \item{y}{Vector with the response variables}
  \item{x}{Matrix or data frame containing the explanatory variables}
  \item{h}{Parameter determining the trimming}
  \item{adjust}{Whether to perform intercept adjustment at each step}
  \item{runs}{Number of independent runs}
  \item{generations}{Number of generations after which the algorithm will be stopped}
}
\value{
The function \code{LTSevol} returns an object of class "ltsEA". This object contains:
  \item{summary}{Summary of the FrEAK run}
  \item{best}{The best subset found}
  \item{coefficients}{Vector of coefficient estimates}
  \item{crit}{The value of the objective function of the LTS regression method, i.e., the sum of the h smallest squared raw residuals}
}
\references{O. Morell, T. Bernholt, R. Fried, J. Kunert, and R. Nunkesser (2008). An Evolutionary Algorithm for LTS-Regression: A Comparative Study. \emph{Proceedings of COMPSTAT 2008.} To Appear. 

  P. J. Rousseeuw (1984), Least Median of Squares Regression. \emph{Journal of the American Statistical Association} \bold{79}, 871--881.

}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{
  \code{"\linkS4class{ltsEA}"}
}
\examples{
# load example data
data(stackloss)

# compute LTS regression
LTSevol(stackloss[, 4],stackloss[, 1:3],adjust=TRUE,runs=1,generations=1000)
}
\keyword{robust}
\keyword{interface}
