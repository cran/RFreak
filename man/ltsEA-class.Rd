\name{ltsEA-class}
\alias{ltsEA-class}
\alias{show,ltsEA-method}
\docType{class}
\title{Class "ltsEA" }
\description{Encapsulates information returned from FrEAK computing an LTS regression.}
\section{Objects from the Class}{
An \code{ltsEA} object holds four slots.
}
\section{Slots}{
	 \describe{
    \item{\code{summary}:}{A \code{data.frame} with a summary of the FrEAK run (inherited from \code{"\linkS4class{FreakReturn}"})}
    \item{\code{best}:}{The best subset found}
    \item{\code{coefficients}:}{Vector of coefficient estimates}
    \item{\code{crit}:}{The value of the objective function of the LTS regression method, i.e., the sum of the \eqn{h} smallest squared raw residuals}
  }
}
\section{Extends}{
Class \code{"FreakReturn"}, directly.
}
\author{Robin Nunkesser \email{Robin.Nunkesser@tu-dortmund.de}}
\seealso{
  \code{"\linkS4class{FreakReturn}"}, \code{\link{LTSevol}}
}
\keyword{classes}