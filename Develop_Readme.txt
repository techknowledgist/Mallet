Changes made by Gitit Kehat to mallet version 2.0.8 in order to 
(1) use the svm format as advertised.  This allows us to input features and their counts (label<tab>feature:count<space>feature:count...)
(2) print classification results along with the term (identifier for the item classified).  Result file format: term<tab>label1<tab>probability1<tab>label2<tab>probability2...
(3) support a new command line tag for the vectors2info command:  --print-infogain <number> which outputs a new file with <number> features sorted by infogain 

List of changes and their locations:

1. In "/home/j/llc/gititkeh/mallet-2.0.8/src/cc/mallet/classify/tui/Vectors2Vectors.java", starting from line 256:
A new pipe for prune-infogain. This option handled Svm form only for training.
Uses the same pipe as in the training - SvmLight2FeatureVectorAndLabel, so it will work
for this format also in classifier.

2. In "/home/j/llc/gititkeh/mallet-2.0.8/src/cc/mallet/pipe/SvmLight2FeatureVectorAndLabel.java", line 45:
A new pipe constructor that can be initialized on given alphabets (to be used in change 1).

For both changes we used comments from:
http://comments.gmane.org/gmane.comp.ai.mallet.devel/1864 (which is offline as of June 2018)

3. In "/home/j/llc/gititkeh/mallet-2.0.8/src/cc/mallet/classify/tui/SvmLight2Classify.java", line 107-112:
a.Regexp filtering is commented so there is no punctuation filtering (this is the default and there is no command line flag to control it in svm2classify).
I used this in our case since we do have special marks sometimes.
b.Additional argument "true" for "SelectiveFileLineIterator", see change 5.

4. In "/home/j/llc/gititkeh/mallet-2.0.8/src/cc/mallet/types/InstanceList.java", lines
 323-342:
All these lines are commented.
In our case, we input in the classification the terms (the svm format expects only labels and features). 
Therefore, the svm pipe interprates the terms as the labels and the test in the below line is never true 
(since the labels alphabet is different than the terms alphabet).

5. In "/home/j/llc/gititkeh/mallet-2.0.8/src/cc/mallet/pipe/iterator/SelectiveFileLineIterator.java" - 
a special change in order to present the terms in the result of the svm2classify. Without the change, the terms are ignored and replaced by "Array i". 
a. I added in line 29 a new boolean parameter to specify not to ignore the terms.
b. I added in lines 38-47 a new constructor "SelectiveFileLineIterator" that gets a bool value. When this value (flag) 
is checked to be true (which is currently the default for svm format),
the terms are stored and printed instead of the uri ("Array i"). Handling the flag in lines 69-73 and 80-87.  

6. In script /home/j/llc/gititkeh/mallet-2.0.8/bin/vectors2info, I changed in line 10 the classpath to ../lib instead lib so mallet will find the main class in lib folder for vectors2info.

7. In script /home/j/llc/gititkeh/mallet-2.0.8/bin/mallet, I changed in line 8 the classpath to ../class instead class so mallet will find the main class created in class folder when running mallet

Gitit Kehat compiled the system with these changes with ant tool on windows and copied into linux machine in:
/home/j/llc/gititkeh/mallet-2.0.8

To build mallet:
1. Install ant tool and add ANT_HOME environment variable and ANT_HOME/bin to the path as explained in https://ant.apache.org/manual/install.html
2. Add MALLET_HOME environent varible, switch to MALLET_HOME directory and press "ant", as explained in http://mallet.cs.umass.edu/download.php
3. To create one jar file for Mallet, run in MALLET_HOME directory "ant jar", the jar file will be creatied in "dist" folder.
