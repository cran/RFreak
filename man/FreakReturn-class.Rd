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
	The only slot \code{summary} contains a \code{data.frame} with the following informations about the individuals of an executed FrEAK run:
	 \describe{
	  \item{run}{The run the individual was found in}
	  \item{generation}{The generation the individual was created in}
	  \item{objective value(s)}{The objective value(s) as returned by the fitness function}
	  \item{individual}{A string representation of the individual}
  }
}
\author{Robin Nunkesser \email{Robin.Nunkesser@tu-dortmund.de}}
\seealso{\code{"\linkS4class{ltsEA}"}, \code{"\linkS4class{GPAS}"}}
\keyword{classes}