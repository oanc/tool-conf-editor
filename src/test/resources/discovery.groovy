ldc 'Kidnap Corpus (Gigaword)', {
    ['list', 'get', 'kidnap_Collection', 'kidnap_collection_id'].each {
        tool "lapps_gigaword/${it}.xml"
    }
}

discovery 'Discovery 2020', {
    [
     'passage_extractor',
     'scoring',
     'summarize',
     'ner_span_annotator',
     'ner_span_annotator2',
     'multi_annotator',
     'multi_annotator2',
     'get'
    ].each {
        tool "discovery/${it}.xml"
    }
}

/*
        <tool file="lapps_gigaword/list.xml"/>
        <tool file="lapps_gigaword/get.xml"/>
        <tool file="lapps_gigaword/kidnap_collection.xml"/>
        <tool file="lapps_gigaword/kidnap_collection_id.xml"/>

		<tool file="discovery/passage_extractor.xml"/>
		<tool file="discovery/scoring.xml"/>
		<tool file="discovery/summarize.xml"/>
		<tool file="discovery/ner_span_annotator.xml"/>
		<tool file="discovery/ner_span_annotator2.xml"/>
		<tool file="discovery/multi_annotator.xml"/>
		<tool file="discovery/multi_annotator2.xml"/>
		<tool file="discovery/get.xml"/>

 */