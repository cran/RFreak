\name{evolreg-class}
\alias{evolreg-class}
\alias{show,evolreg-method}
\alias{ltsEA-class}
\alias{show,ltsEA-method}
\docType{class}
\title{Class "evolreg" }
\description{Encapsulates information returned from FrEAK computing robust regression. For compatibility reasons also called \code{ltsEA}.}
\section{Objects from the Class}{
An \code{evolreg} object holds four slots.
}
\section{Slots}{
	 \describe{
    \item{\code{summary}:}{A \code{data.frame} with a summary of the FrEAK run (inherited from \code{"\linkS4class{FreakReturn}"})}
    \item{\code{best}:}{The best subset found}
    \item{\code{coefficients}:}{Vector of coefficient estimates}
    \item{\code{crit}:}{The value of the objective function of the used regression method}
  }
}
\section{Extends}{
Class \code{"FreakReturn"}, directly.
}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{
  \code{"\linkS4class{FreakReturn}"}, \code{\link{robreg.evol}}
}
\keyword{classes}