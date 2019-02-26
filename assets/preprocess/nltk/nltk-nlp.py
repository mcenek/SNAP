#!/usr/bin/env python
import os
import sys
import string

import nltk
from nltk.corpus import stopwords
from nltk.corpus import wordnet as wn

#nltk.download('punkt')
#nltk.download('averaged_perceptron_tagger')
#nltk.download('wordnet')
#nltk.download('maxent_ne_chunker')
#nltk.download('words')

from nltk.tree import ParentedTree
from nltk.stem import WordNetLemmatizer
from nltk.corpus import wordnet
from nltk.tokenize import PunktSentenceTokenizer


def get_wordnet_pos(treebank_tag):
	if treebank_tag.startswith('J'):
		return wordnet.ADJ
	elif treebank_tag.startswith('V'):
		return wordnet.VERB
	elif treebank_tag.startswith('N'):
		return wordnet.NOUN
	elif treebank_tag.startswith('R'):
		return wordnet.ADV
	else:
		return ''

def token_process(file_in):
	_tokens = nltk.word_tokenize(file_in)
	return _tokens

def sent_process(file_in):
	_sents = nltk.sent_tokenize(file_in)
	return _sents

def pos_process(file_in):
	_pos = []
	_sents = nltk.sent_tokenize(file_in)
	for sent in _sents:
		_tokens = nltk.word_tokenize(sent)
		_pos_tokens = nltk.pos_tag(_tokens)
		for token in _pos_tokens:
			pos = '{}/{}'.format(token[0].encode("ascii", "ignore"),token[1])
			_pos.append(pos)
	return _pos

def lemma_process(file_in):
	_lemmas = []
	_sents = nltk.sent_tokenize(file_in)
	wordnet_lemmatizer = WordNetLemmatizer()
	for sent in _sents:
		_pos = nltk.pos_tag(nltk.word_tokenize(sent))
		for pos in _pos:
			tag = get_wordnet_pos(pos[1])
			lem = ''
			if tag != '':
				lem = '{}/{}/{}'.format(pos[0].encode("ascii", "ignore"),
						pos[1],
						wordnet_lemmatizer.lemmatize(pos[0], tag).encode("ascii", "ignore"))
			else:
				lem = '{}/{}/{}'.format(pos[0].encode("ascii", "ignore"),
						pos[1],
						wordnet_lemmatizer.lemmatize(pos[0]).encode("ascii", "ignore"))
			_lemmas.append(lem)
	return _lemmas

SENTENCE = 'S'
_ners = []
first = 1 
current = ''

def ner_process(parent):
	ner = ''
	global SENTENCE, _ners, first, current
	for node in parent:
		if type(node) is nltk.Tree:
			if node.label() == SENTENCE:
				sys.stdout.write('')
			else:
				for leaf in node.leaves():
					ner = '{}/{}/{}'.format(leaf[0].encode("ascii", "ignore"), leaf[1], node.label())
					current = leaf[0]
					_ners.append(ner)
			ner_process(node)
		else:
			if first == 1:
				ner = '{}/{}'.format(node[0].encode("ascii", "ignore"), node[1])
				first = 0 
				_ners.append(ner)
			elif node[0] != current:
				ner = '{}/{}'.format(node[0].encode("ascii", "ignore"), node[1])
				_ners.append(ner)
			else:
				pass
	return _ners

	#sample_text = "The world is a strange place. Filled with cats and dogs. Guam is an island in the Pacific Ocean. The United States of America is a grand place with Donald Trump as President. Canada is Americas hat. I used to work for Alaska's Division of Election."
	#custom_sent_tokenizer = PunktSentenceTokenizer(sample_text)
	#tokenized = custom_sent_tokenizer.tokenize(sample_text)

#def ner_process(file_in):
	#namedEnt = ""
	#target = open("target.txt", 'w')
	#print(type(stringFile))
	#custom_sent_tokenizer = PunktSentenceTokenizer(sample_text)
	#tokenized = custom_sent_tokenizer.tokenize(sample_text)

#	words = nltk.word_tokenize(file_in)
#	tagged = nltk.pos_tag(words)
#	namedEnt = nltk.ne_chunk(tagged, binary=True)
#	sys.stdout.write(namedEnt)
 
#	f = open('/var/www/html/SNAP/cenek@up.edu/project_one/preprocessed/NER_Test.txt', 'w')
#	f.write("blah blah")
#	f.write(" ".join(namedEnt))
	#f.write(str(namedEnt))
	#f.write(str('this is it'))
#	f.close()
		
	#target.write(str(namedEnt))
#	return namedEnt

def main():
	doc = []
	sents = []
	pos_tagged = []
	lemmas = []
	ner_tagged = []

	file = ''
	
	with open(sys.argv[1], 'r') as _file:
		file = _file.read()

	if file == '':
		print 'File not read properly'
		sys.exit()

	uni_str = unicode(file, encoding="utf-8")
	if sys.argv[-1] == 'tokenize':
		doc = token_process(uni_str)
		sys.stdout.write(" ".join(doc).encode("ascii", "ignore"))
		#for token in doc:
		#	print token

	elif sys.argv[-1] == 'sent_split':
		sents = sent_process(uni_str)
		for sent in sents:
			print sent.encode("ascii", "ignore")

	elif sys.argv[-1] == 'pos_tag':
		pos_tagged = pos_process(uni_str)
		#print pos_tagged
		#for pos in pos_tagged:
		#	#print pos 
		#	sys.stdout.write(pos)
		sys.stdout.write("\n".join(pos_tagged))

		#f = open("test_write.txt", "w")
		#f.write("\n".join(pos_tagged))
		#f.close()

	elif sys.argv[-1] == 'lemmatize':
		lemmas = lemma_process(uni_str)
		#for lemma in lemmas:
			#print lemma
		#	sys.stdout.write(lemma)
		
		sys.stdout.write(" ".join(lemmas))

	elif sys.argv[-1] == 'ner_tag':
		stemmer = nltk.stem.PorterStemmer()
		sents = nltk.sent_tokenize(uni_str)
		tokens = [nltk.word_tokenize(s) for s in sents]
		# tokens_out = [];
		# for s in tokens:
		# 	new_list=[];
		# 	new_list = [new_list.append(stemmer.stem(w)) for w in s]
		# 	tokens_out.append(new_list);
		pos = [nltk.pos_tag(s) for s in tokens]
		chunked = nltk.ne_chunk_sents(pos, binary=False)
		ners = ner_process(chunked)	
		stop = stopwords.words('english') + list(string.punctuation)
		out = [i for i in ners if i.lower().split('/')[0] not in stop]
		for o in out:
			stem = stemmer.stem(o.split('/')[0]);
			if stem not in ['\'s','\'','\`','"','\'\'', '``', '\"\"']:
				#stem = stemmer.stem(o.split('/')[0]);
				#if len(stem)>2: 	#a hack to get rid of ''  and `` and "" strings
				ot=stem+"/"+o.split('/')[1];
				ot=ot.replace("-","");
				ot=ot.replace(".","");
				ot=ot.replace("'","");
				sys.stdout.write(str(ot)+" " + str(len(stem))+ " "+ str(o)+" \n");

		#words = nltk.word_tokenize(uni_str)
		#tagged = nltk.pos_tag(words)
		#namedEnt = nltk.ne_chunk(tagged, binary=True)

		# this might be easier and cleaner, but the output file name 
		# has to be passed in as sys.argv[1] a param before POST vars
		#f = open('/var/www/html/SNAP/cenek@up.edu/project_one/preprocessed/NER_Test.txt', 'w')
		#f.write(str(ners))
		#f.close()
	else:
		sys.stdout.write("Could not process files, invalid tag")
	#open("test_write.txt", "w")

if __name__ == '__main__':
    main()

#
#inpt = ""
#tokenCount = 0
#
#with open('_raw/posCase-v5.txt', 'r') as f:
#	inpt=f.read().replace('\n', ' ')
#
#ustr = unicode(inpt,encoding="utf-8")
##only use sent_toknize_list for sent splitting
#sent_tokenize_list = nltk.sent_tokenize(ustr)
#
#word_tokenize_list = []
#pos_word_list = []
#for sent in sent_tokenize_list:
#	#TOKENIZE SENT INTO INDIVIDUAL TOKENS
#	word_tokenize_list = nltk.word_tokenize(sent)
#	#POS TAG WORD TOKENS
#	pos_word_list = nltk.pos_tag(word_tokenize_list)
#	for pos in pos_word_list:
#		tokenCount+=1
#		print '{} {}'.format(pos[0].encode('ascii', 'ignore'), pos[1])
#
