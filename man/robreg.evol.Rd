\name{robreg.evol}
\alias{robreg.evol}
\alias{lmsreg.evol}
\alias{lqdreg.evol}
\alias{lqsreg.evol}
\alias{ltareg.evol}
\alias{ltsreg.evol}
\title{Robust Evolutionary Regression}
\description{
Carries out robust regression with an evolutionary algorithm. \code{ltsreg}, \code{ltareg}, \code{lmsreg}, \code{lqsreg}, and \code{lqdreg} are wrappers. 
}
\usage{
robreg.evol(x, y, method = c("lts", "lta", "lms", "lqs", "lqd"), 
	quantile=NULL, adjust=FALSE, runs=1, generations=10000, duration=0)

## Wrappers:
ltsreg.evol(\dots)
ltareg.evol(\dots)
lmsreg.evol(\dots)
lqsreg.evol(\dots)
lqdreg.evol(\dots)
}
\arguments{
  \item{x}{Matrix or data frame containing the explanatory variables}
  \item{y}{Vector with the response variables}
  \item{method}{The method to be used. One of "lts", "lta", "lms", "lqs", and "lqd".}
  \item{quantile}{The quantile to be used: see \code{Details}.}
  \item{adjust}{Whether to perform intercept adjustment at each step}
  \item{runs}{Number of independent runs}
  \item{generations}{Number of generations after which the algorithm will be stopped}
  \item{duration}{Duration in seconds after which the algorithm will be stopped}
  \item{\dots}{Arguments to be passed to the default method}
}
\value{
The function \code{robreg.evol} returns an object of class "evolreg". This object contains:
  \item{summary}{Summary of the FrEAK run}
  \item{best}{The best subset found}
  \item{coefficients}{Vector of coefficient estimates}
  \item{crit}{The value of the objective function of the regression method}
}
\details{
  Suppose there are \code{n} data points and \code{p} regressors,
  including any intercept.

  The first four methods minimize some function of the sorted squared
  residuals. For methods \code{"lqs"} and \code{"lms"} it is the
  \code{quantile} squared residual, and for \code{"lts"} (\code{"lts"}) it is the sum
  of the \code{quantile} smallest squared (absolute) residuals. \code{"lqd"} minimizes approximately the quartile of the absolute residual differences.   

}

\references{O. Morell, T. Bernholt, R. Fried, J. Kunert, and R. Nunkesser (2008). An Evolutionary Algorithm for LTS-Regression: A Comparative Study. \emph{Proceedings of COMPSTAT 2008.} To Appear. 

  P. J. Rousseeuw (1984), Least Median of Squares Regression. \emph{Journal of the American Statistical Association} \bold{79}, 871--881.

}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{
  \code{"\linkS4class{evolreg}"}
}
\examples{
# load example data
data(stackloss)

# compute different regressions
robreg.evol(stackloss[, 1:3], stackloss[, 4], method= "lts", generations=1000)
lqsreg.evol(stackloss[, 1:3], stackloss[, 4], generations=1000)
lqdreg.evol(stackloss[, 1:3], stackloss[, 4], generations=1000)
}
\keyword{robust}
\keyword{interface}
