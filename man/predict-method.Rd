\name{predict-method}
\alias{predict-method}
%\alias{predict,ANY-method}
\alias{predict,GPAS-method}
\title{Predict using new predictors}
\description{
Takes an individual from a GPAS object and predicts on the basis of new predictors.
}
\usage{
\S4method{predict}{GPAS}(object,individual,preds)
%## S4 method for signature 'GPAS': 
%predict(object,individual,preds)
}
\arguments{
  \item{object}{Object of class GPAS}
  \item{individual}{Number of the individual to use}
  \item{preds}{New predictors}
}
\value{
  Returns a vector with the new predictions.
}
\author{Robin Nunkesser \email{Robin.Nunkesser@tu-dortmund.de}}
\seealso{
  \code{"\linkS4class{GPAS}"}, \code{\link{GPASDiscrimination}}, \code{\link{GPASInteractions}}
}
\keyword{methods}