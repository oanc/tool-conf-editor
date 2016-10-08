String tokenizer = 'lapps_opennlp/tokenizer.xml'
String splitter = 'lapps_opennlp/splitter.xml'
String tagger = 'lapps_opennlp/tagger.xml'
String ner = 'lapps_opennlp/ner.xml'

opennlp 'Apache OpenNLP', {
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