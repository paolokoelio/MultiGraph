#!/bin/bash

JAVA="/usr/lib/jvm/java-8-oracle/bin/java"

BASE_PATH="/home/julien/git/MultiGraph/files/ags/"

CLASSPATH="/home/julien/git/MultiGraph/bin:/home/julien/git/MultiGraph/lib/jbool_expressions-1.22-SNAPSHOT.jar:/home/julien/git/MultiGraph/lib/jgraphx.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/hamcrest-core-1.3.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/jcommon-1.0.23.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/jfreechart-1.0.19-experimental.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/jfreechart-1.0.19-swt.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/jfreechart-1.0.19.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/jfreesvg-2.0.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/junit-4.11.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/orsoncharts-1.4-eval-nofx.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/orsonpdf-1.6-eval.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/servlet.jar:/home/julien/git/MultiGraph/lib/jfreechart-1.0.19/lib/swtgraphics2d.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/commons-cli-1.2.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/commons-codec-1.8.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/commons-lang3-3.1.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/commons-math3-3.4.1.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/jcommon-1.0.20.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/jfreechart-1.0.15.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/JMetal-4.3.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/MOEAFramework-2.12.jar:/home/julien/git/MultiGraph/lib/MOEAFramework-2.12/lib/rsyntaxtextarea.jar:/home/julien/git/MultiGraph/lib/jdesktop/appframework-1.0.3.jar:/home/julien/git/MultiGraph/lib/jdesktop/swing-layout-1.0.3.jar:/home/julien/git/MultiGraph/lib/jdesktop/swing-worker-1.1.jar:/home/julien/git/MultiGraph/lib/CVSS/CVSS.jar:/home/julien/git/MultiGraph/lib/org-jdesktop-beansbinding.jar:/home/julien/git/MultiGraph/lib/beansbinding-1.2.1.jar:/home/julien/git/MultiGraph/lib/mysql-connector-java-8.0.15.jar:/home/julien/git/MultiGraph/lib/AttackAnalysis.jar:/home/julien/git/MultiGraph/lib/ext-1.1.1.jar:/home/julien/git/MultiGraph/lib/jep-2.4.1.jar:/home/julien/git/MultiGraph/lib/guava-23.0.jar:/home/julien/git/MultiGraph/lib/sqlite-jdbc-3.8.11.1.jar:/home/julien/git/MultiGraph/lib/hsqldb/lib/hsqldb.jar "


#TODO make loop for this guy here
echo "Starting all procedures for AGs in $BASE_PATH"
echo ""

METHODS=('lwang.AttackGraph' 'poolsappasitmoop.BayesianAttackGraphAdapted 1 1 NSGAII 1000 noplot' 'almohri.Main.Main')

for ((j = 0; j < ${#METHODS[@]}; j++))
do

echo ${METHODS[$j]}

	#for i in {1..$(ls -1q files/ags | wc -l)};
	for i in $BASE_PATH*
	do
		
		IFS='/' read -ra DIR <<< "$i"
		DIR_NAME=${DIR[${#DIR[@]}-1]}

		echo "$i/"
		$JAVA -Dfile.encoding=UTF-8 -classpath $CLASSPATH es.um.multigraph.decision.${METHODS[$j]} $i/ > stdouts/stdout_"$DIR_NAME"_"${METHODS[$j]}".txt
		wait

	done

	wait
	echo ""
done
