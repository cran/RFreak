\name{launchScheduleEditor}
\alias{launchScheduleEditor}
\title{Launches a graphical schedule editor}
\description{
Launches a graphical schedule editor for FrEAK schedules. A schedule contains the algorithm and simulation options for the desired evolutionary algorithm.  
}
\usage{
launchScheduleEditor(saveTo = "schedule.freak", load = NULL)
}
\arguments{
  \item{saveTo}{Name of the file the schedule is saved to}
  \item{load}{Name of an (optional) file that should be loaded to the editor}
}
\details{
Setting up a schedule consists of seven algorithm specific steps and two simulation specific steps. 
To set up an evolutionary algorithm it is necessary to choose a search space, a fitness function, an optional genotype-mapper, an algorithm graph, a set of stopping criteria, a population model, and an initial population. 
The simulation specific steps allow the user to choose views and observers (the necessary ones for the R interface are preselected, views depending on the FrEAK GUI are not supported). The last step consists of choosing batches (only one batch supported) and the number of independent runs.
A detailed instruction on how to set up schedules can be found in the User's Guide of FrEAK.
}
\references{FrEAK User's Guide: 

\url{http://ls2-www.cs.uni-dortmund.de/~nunkesser/software/usersguide.pdf}}
\author{Robin Nunkesser \email{Robin.Nunkesser@hshl.de}}
\section{Warning}{
Editing schedules has to be finished by pressing the "Finish" button to obtain a file including the schedule.  The observer "Result" and the view "R Return" - which are preselected - and a stopping criterion are necessary for the R interface to work.
}
\seealso{\code{\link{executeSchedule}}}
\examples{\dontrun{	
# Start the schedule editor and set up a schedule
launchScheduleEditor()
}}
\keyword{interface}
\keyword{optimize}
