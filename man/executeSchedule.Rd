\name{executeSchedule}
\alias{executeSchedule}
\title{Executes a FrEAK schedule}
\description{
Executes a schedule created by the FrEAK schedule editor and returns a summary of the result.
}
\usage{
executeSchedule(freakfile = "schedule.freak")
}
\arguments{
  \item{freakfile}{File containing the schedule to be executed}
}
\value{
  Returns an object of class \code{FreakReturn} enwrapping a \code{data.frame} in its only slot \code{summary} containing information about the last population of the executed schedule. 
  For each individual in the last population the following information is contained:
  \item{run}{The run the individual was found in}
  \item{generation}{The generation the individual was created in}
  \item{objective value(s)}{The objective value(s) as returned by the fitness function}
  \item{individual}{A string representation of the individual}
}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\section{Warning}{
To obtain a result, the schedule needs to have a stopping criterion and the observer "Result" and the view "R Return" which are automatically preselected when using \code{launchScheduleEditor}.
}
\seealso{\code{\link{launchScheduleEditor}}, \code{"\linkS4class{FreakReturn}"}}
\examples{\dontrun{	
# Start the schedule editor and set up a schedule
launchScheduleEditor()

# Execute the set up schedule. 
executeSchedule()
}}
\keyword{interface}
\keyword{optimize}
