document.addEventListener("DOMContentLoaded", function(event){
	console.log("DOM fully loaded and parsed");

	var preprocesses = document.getElementsByClassName("preprocess");

	for(var i = 0; i < preprocesses.length; i++){
		preprocesses[i].addEventListener("change", checkProcesses, false);
	}
	var hidden = document.querySelectorAll('[data-active=false]');
	for(var i = 0; i < hidden.length; i++){
		hidden[i].style.visibility = 'hidden';
	}
});

//TODO: need to do more checks to ensure conflicting processes can't occur
//the passed values are validated once more on the server side in the php code,
//this is mainly for user's ease of use
function getSelectedText(elementId){
	var el = document.getElementById(elementId);
	if(el.selectedIndex == -1){
		return null;
	}
	return el.options[el.selectedIndex].text;
}

function checkProcesses(){
	var stem = document.getElementById('stemming');
	var sent = document.getElementById('sent_split');
	var pos = document.getElementById('pos_tag');
	var lemma = document.getElementById('lemmatize');
	var ner = document.getElementById('ner_tag');

	switch(this.id){
		case 'tokenize':
			if(sent.dataset.active == 'false'){ 
				if(this.value == 'corenlp'){
					var option = 'CoreNLP';
					var value = 'corenlp';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					sent.appendChild(el);
				}
				else if(this.value == 'nltk'){
					var option = 'NLTK';
					var value = 'nltk';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					sent.appendChild(el);
				}
				else if(this.value == 'spacy'){
					var option = 'spaCy';
					var value = 'spacy';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					sent.appendChild(el);
				}
				sent.style.visibility = 'visible';
				sent.dataset.active = 'true';
			} else {
				var activeProcesses = document.querySelectorAll('[data-active=true]');
				for(var i = 0; i < activeProcesses.length; i++){
					if(activeProcesses[i].options[1].text != this.options[this.selectedIndex].text){
						activeProcesses[i].remove(1);
						var option = getSelectedText('tokenize');
						var value = this.value;
						var el = document.createElement("option");
						el.text = option;
						el.value = value;
						activeProcesses[i].add(el, activeProcesses[i].options[1]);
					}
				}
			}
			break;

		case 'sent_split':
			if(pos.dataset.active == 'false'){ 
				if(this.value == 'corenlp'){
					var option = 'CoreNLP';
					var value = 'corenlp';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					pos.appendChild(el);
				}
				else if(this.value == 'nltk'){
					var option = 'NLTK';
					var value = 'nltk';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					pos.appendChild(el);
				}
				else if(this.value == 'spacy'){
					var option = 'spaCy';
					var value = 'spacy';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					pos.appendChild(el);
				}
				pos.style.visibility = 'visible';
				pos.dataset.active = 'true';
			}
			break;

		case 'pos_tag':
			if(lemma.dataset.active == 'false'){
				if(this.value == 'corenlp'){
					var option = 'CoreNLP';
					var value = 'corenlp';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					lemma.appendChild(el);
				}
				else if(this.value == 'nltk'){
					var option = 'NLTK';
					var value = 'nltk';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					lemma.appendChild(el);
				}
				else if(this.value == 'spacy'){
					var option = 'spaCy';
					var value = 'spacy';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					lemma.appendChild(el);
				}
				lemma.style.visibility = 'visible';
				lemma.dataset.active = 'true';
			}
			break;

		case 'lemmatize':
			if(ner.dataset.active == 'false'){
				if(this.value == 'corenlp'){
					var option = 'CoreNLP';
					var value = 'corenlp';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					ner.appendChild(el);
				}
				else if(this.value == 'nltk'){
					var option = 'NLTK';
					var value = 'nltk';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					ner.appendChild(el);
				}
				else if(this.value == 'spacy'){
					var option = 'spaCy';
					var value = 'spacy';
					var el = document.createElement("option");
					el.textContent = option;
					el.value = value;
					ner.appendChild(el);
				}
				ner.style.visibility = 'visible';
				ner.dataset.active = 'true';
			}
			break;
	}
}
