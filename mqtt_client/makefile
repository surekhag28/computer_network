JFLAGS=-g
JC=javac -d ./classes -cp "./lib/org.eclipse.paho.client.mqttv3-1.2.1.jar"	
JVM=java -cp .:./classes
SRCDIR= ./src/edu/mqtt/au
FILES= $(SRCDIR)/Statistics.java $(SRCDIR)/GenerateReport.java $(SRCDIR)/Subscriber.java $(SRCDIR)/Publisher.java

.SUFFIXES:.java .class
.java.class:
		$(JC) $(JFLAGS) $(FILES)

CLASSES= \
				 $(SRCDIR)/Statistics.java \

MAIN = edu.mqtt.au.Subscriber

default: classes
classes: $(CLASSES:.java=.class)

run: classes
		$(JVM) $(MAIN)

clean:
		$(RM) ./classes/edu/mqtt/au/*.class


