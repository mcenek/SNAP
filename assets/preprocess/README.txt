Place the text preprocessing frameworks with evolved models into these sub-directories

NLTK:
1. make sure that the NLTK is installed on your syste:
	sudo pip install nltk

2. the models must be downloaded into the system wide shared folder o.w. the models are only
   visible to the user. Run the following:
   python
   import nltk
   nltk.download()

   when pop up windown shows change the download directory to: 
   MAC: /usr/local/share/nltk_data  LINUX: /usr/share/nltk_data

   alternatively, from command line you can run:
   "sudo python -m nltk.downloader -d <system path from above> all"


