#!/bin/bash

set -e

# Launch $1 instances of testProdCons in different tabs of the same terminal
# and run testProdCons.sh in the current terminal

# $1 is the number of instances to launch

# Launch the instances
for ((i=0; i<$1; i++))
do
    gnome-terminal --tab --title="testProdCons$i" --command="bash -c \"/home/lilian/.jdks/openjdk-19.0.1/bin/java -javaagent:/snap/intellij-idea-ultimate/398/lib/idea_rt.jar=39709:/snap/intellij-idea-ultimate/398/bin -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /home/lilian/info-4/s8/others/pc7-project/out/production/pc7-project prodcons.TestProdCons; exec bash\""
done






