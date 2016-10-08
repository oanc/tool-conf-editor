String tokenizer = 'lapps_lingpipe/tokenizer.xml'
String splitter = 'lapps_lingpipe/splitter.xml'
String tagger = 'lapps_lingpipe/tagger.xml'
String ner = 'lapps_lingpipe/ner.xml'

lingpipe 'Lingpipe', {
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