import csv 
import math
import sys
import re

trainPath=sys.argv[1]
testPath=sys.argv[2]
stopwordsPath=sys.argv[3]

f=open(stopwordsPath,'r')
temp=f.read()
stopWords=re.split('\W+',temp)
dictionary={}
for word in stopWords:
	dictionary[word]=""


hamPath=trainPath+"\\ham"

from os import listdir
from os.path import isfile, join

hamfiles=[f for f in listdir(hamPath) if isfile(join(hamPath,f))]

hamText=""
totalHamFiles=len(hamfiles)
for a in range(0,totalHamFiles,1):
	f=open(hamPath+"\\"+hamfiles[a],'r')
	temp=f.read()
	hamText=hamText+temp
HamTotalText=hamText

for word,initial in dictionary.items():
	HamTotalText=HamTotalText.lower()
	HamTotalText=HamTotalText.replace(" "+word.lower()+" "," "+initial+" ")

spamPath=trainPath+"\\spam"
spamfiles=[f for f in listdir(spamPath) if isfile(join(spamPath,f))]

spamText=""
totalSpamFiles=len(spamfiles)
for a in range(0,totalSpamFiles,1):
	f=open(spamPath+"\\"+spamfiles[a],'r')
	temp=f.read()
	spamText=spamText+temp
spamTotalText=spamText

for word,initial in dictionary.items():
	spamTotalText=spamTotalText.lower()
	spamTotalText=spamTotalText.replace(" "+word.lower()+" "," "+initial+" ")

HamPriorProb=float(totalHamFiles)/(totalHamFiles+totalSpamFiles)
SpamPriorProb=1-HamPriorProb

print(HamPriorProb)
print(SpamPriorProb)

HamTokenized=re.split('\W+',HamTotalText)
SpamTokenized=re.split('\W+',spamTotalText)

SpamFreqTable={}
for token in range(0,len(SpamTokenized),1):
	if SpamTokenized[token] in SpamFreqTable:
		SpamFreqTable[SpamTokenized[token]]=SpamFreqTable[SpamTokenized[token]]+1
	else:
		SpamFreqTable[SpamTokenized[token]]=1

HamFreqTable={}
for token in range(0,len(HamTokenized),1):
	if HamTokenized[token] in HamFreqTable:
		HamFreqTable[HamTokenized[token]]=HamFreqTable[HamTokenized[token]]+1
	else:
		HamFreqTable[HamTokenized[token]]=1
	if not HamTokenized[token] in SpamFreqTable:
		SpamFreqTable[HamTokenized[token]]=0

for token in range(0,len(SpamTokenized),1):
	if not SpamTokenized[token] in HamFreqTable:
		HamFreqTable[SpamTokenized[token]]=0

print('Frequency of Ham table is :')
print(len(HamFreqTable))
print('Frequency of Spam table is :')
print(len(SpamFreqTable))


SpamCondProb={}
spamDistinctWords=0

for keys in SpamFreqTable:
	spamDistinctWords=spamDistinctWords+SpamFreqTable[keys]+1
for keys in SpamFreqTable:
	SpamCondProb[keys]=float(SpamFreqTable[keys]+1)/spamDistinctWords


HamCondProb={}
HamDistinctWords=0
for keys in HamFreqTable:
	HamDistinctWords=HamDistinctWords+HamFreqTable[keys]+1
for keys in SpamFreqTable:
	HamCondProb[keys]=float(HamFreqTable[keys]+1)/HamDistinctWords

cc=0
sizeOfClassification=0


testHamPath=testPath+"\\ham"

from os import listdir
from os.path import isfile, join

hamfiles=[f for f in listdir(testHamPath) if isfile(join(testHamPath,f))]

hamText=""
totalHamFiles=len(hamfiles)
print('HamFilesT')
print(totalHamFiles)
for a in range(0,len(hamfiles),1):
	f=open(testHamPath+"\\"+hamfiles[a],'r')
	temp=f.read()
	hamText=hamText+temp
	sizeOfClassification=sizeOfClassification+1
	hamScore=math.log(HamPriorProb)
	spamScore=math.log(SpamPriorProb)
	tokenizedData=re.split('\W+',temp)
	
	for i in tokenizedData:
		if i in SpamCondProb:
			spamScore=spamScore+math.log(SpamCondProb[i]) 

	for i in tokenizedData:
		if i in HamCondProb:
			hamScore=hamScore+math.log(HamCondProb[i]) 	
	if hamScore>spamScore:
		cc=cc+1

print('Ham files correctly classified:')
print(cc)
testSpamPath=testPath+"\\spam"


spamfiles=[f for f in listdir(testSpamPath) if isfile(join(testSpamPath,f))]

spamText=""
totalSpamFiles=len(spamfiles)
print('Total Spam Fls:')
print(totalSpamFiles)
for a in range(0,totalSpamFiles,1):
	f=open(testSpamPath+"\\"+spamfiles[a],'r')
	temp=f.read()
	sizeOfClassification=sizeOfClassification+1
	hamScore=math.log(HamPriorProb)
	spamScore=math.log(SpamPriorProb)
	tokenizedData=re.split('\W+',temp)
	
	for i in tokenizedData:
		if i in SpamCondProb:
			spamScore=spamScore+math.log(SpamCondProb[i]) 

	for i in tokenizedData:
		if i in HamCondProb:
			hamScore=hamScore+math.log(HamCondProb[i]) 
	if hamScore<spamScore:
		cc=cc+1
print('No:of files correctly classified:')
print(cc)
print('Total No:of files :')
print(sizeOfClassification)

print('The accuracy of Naive Bayes Algorithm is below:')

accuracy=float(cc)/sizeOfClassification

print(accuracy)





