\name{FreakReturn-class}
\alias{FreakReturn-class}
\alias{show,FreakReturn-method}
\docType{class}
\title{Class "FreakReturn" }
\description{Encapsulates information returned from FrEAK}
\section{Objects from the Class}{
A \code{FreakReturn} object holds only one slot, containing a \code{data.frame} with the summary of an executed FrEAK run.
}
\section{Slots}{
	 \describe{
    \item{\code{summary}:}{A \code{data.frame} with informations for each returned individual on the run the individual was found in, the generation the individual was created in, the objective value(s) as returned by the fitness function, and the individual itself.}
  }
}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{\code{"\linkS4class{ltsEA}"}, \code{"\linkS4class{GPAS}"}}
\keyword{classes}