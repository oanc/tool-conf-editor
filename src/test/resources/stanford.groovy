String tokenizer = 'lapps_stanford/tokenizer.xml'
String splitter = 'lapps_stanford/splitter.xml'
String tagger = 'lapps_stanford/tagger.xml'
String ner = 'lapps_stanford/ner.xml'

stanford 'Stanford NLP', {
    tools tokenizer, splitter, tagger, ner
}

tokenizers 'Tokenizers', {
    tool tokenizer
}

splitters 'Sentence Splitters', {
    tool splitter
}

taggers 'POS Taggers',{
    tool tagger
}

'named.entity' 'Named Entity Recognizers', {
    tool ner
}