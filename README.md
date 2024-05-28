### Usage
- install docker and docker-compose

cd RootComponent
sbt docker:publishLocal

cd BoardComponent
sbt docker:publishLocal

cd FileIOComponent
sbt docker:publishLocal

cd new_chess
docker-compose run rootcomponent

--> the tui should start and you can type in commands

// if you want to change code, you have to do "sbt docker:publishLocal" again.
// if you just want to run the program again "docker-compose run rootcomponent" is enaugh.



