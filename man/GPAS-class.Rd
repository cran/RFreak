\name{GPAS-class}
\alias{GPAS-class}
\alias{show,GPAS-method}
\docType{class}
\title{Class "GPAS" }
\description{Encapsulates information returned from FrEAK computing the GPAS algorithm.}
\section{Objects from the Class}{
A \code{GPAS} object holds two slots.
}
\section{Slots}{
	 \describe{
    \item{\code{summary}:}{A \code{data.frame} with a summary of the FrEAK run (inherited from \code{"\linkS4class{FreakReturn}"})}
    \item{\code{trees}:}{The Java objects representing the individuals returned by the FrEAK run}
  }
}
\section{Methods}{
  \describe{
    \item{predict}{Method to obtain predictions of an individual based on new predictors}
	 }
}
\section{Extends}{
Class \code{"FreakReturn"}, directly.
}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\seealso{
  \code{"\linkS4class{FreakReturn}"}, \code{\link{GPASDiscrimination}}, \code{\link{GPASInteractions}}
}
\keyword{classes}