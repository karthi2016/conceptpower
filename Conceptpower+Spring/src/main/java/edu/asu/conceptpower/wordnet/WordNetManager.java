package edu.asu.conceptpower.wordnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.lucene.LuceneUtility;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.WordID;

public class WordNetManager {

    @Autowired
    private WordNetConfiguration configuration;

    @Autowired
    private LuceneUtility luceneUtility;

    private String lucenePath;
    
    Directory index; 
    
    private IDictionary dict;

    private Map<String, POS> posMap;

    @PostConstruct
    public void init() throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        
        Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
        index = FSDirectory.open(relativePath);
        
        String wnhome = configuration.getWordnetPath();
        String path = wnhome + File.separator + configuration.getDictFolder();
        
        URL url = null;

        url = new URL("file", null, path);

        dict = new Dictionary(url);
        dict.open();
        
        System.setProperty("lucenePath", lucenePath);
        
        Iterator<IIndexWord> iterator = dict.getIndexWordIterator(POS.NOUN);
//        int i = 0;
//        for (; iterator.hasNext();) {
//            IIndexWord indexWord = iterator.next();
//            List<IWordID> wordIdds = indexWord.getWordIDs();
//
//            IndexWriterConfig config = new IndexWriterConfig(analyzer);
//            IndexWriter w = new IndexWriter(index, config);
//            i++;
//            if(i == 1000){
//                break;
//            }
//            for (IWordID wordId : wordIdds) {
//               
//                Document doc = new Document();
//                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
//                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));
//
//                IWord word = dict.getWord(wordId);
//                doc.add(new StringField("description", word.getSynset().getGloss(), Field.Store.YES));
//                doc.add(new StringField("id", word.getID().toString(), Field.Store.YES));
//                
//
//                ISynset synset = word.getSynset();
//                List<IWord> synonyms = synset.getWords();
//                StringBuffer sb = new StringBuffer();
//                for (IWord syn : synonyms) {
//                    if (!syn.getID().equals(word.getID()))
//                        sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
//                }
//                doc.add(new StringField("synonymId", sb.toString(), Field.Store.YES));
//
//                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
//                w.addDocument(doc);
//            }
//
//            w.close();
//        }

//        iterator = dict.getIndexWordIterator(POS.ADVERB);
//        
//        for (; iterator.hasNext();) {
//
//            IIndexWord indexWord = iterator.next();
//            List<IWordID> wordIdds = indexWord.getWordIDs();
//
//            IndexWriterConfig config = new IndexWriterConfig(analyzer);
//            IndexWriter w = new IndexWriter(index, config);
//            for (IWordID wordId : wordIdds) {
//                Document doc = new Document();
//                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
//                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));
//
//                IWord word = dict.getWord(wordId);
//                doc.add(new StringField("description", word.getSynset().getGloss(), Field.Store.YES));
//                doc.add(new StringField("id", word.getID().toString(), Field.Store.YES));
//                w.addDocument(doc);
//
//                ISynset synset = word.getSynset();
//                List<IWord> synonyms = synset.getWords();
//                StringBuffer sb = new StringBuffer();
//                for (IWord syn : synonyms) {
//                    if (!syn.getID().equals(word.getID()))
//                        sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
//                }
//                doc.add(new StringField("synonymId", sb.toString(), Field.Store.YES));
//
//                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
//            }
//
//            w.close();
//
//        }
//
//        iterator = dict.getIndexWordIterator(POS.ADJECTIVE);
//        for (; iterator.hasNext();) {
//
//            IIndexWord indexWord = iterator.next();
//            List<IWordID> wordIdds = indexWord.getWordIDs();
//
//            IndexWriterConfig config = new IndexWriterConfig(analyzer);
//            IndexWriter w = new IndexWriter(index, config);
//            for (IWordID wordId : wordIdds) {
//                Document doc = new Document();
//                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
//                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));
//
//                IWord word = dict.getWord(wordId);
//                doc.add(new StringField("description", word.getSynset().getGloss(), Field.Store.YES));
//                doc.add(new StringField("id", word.getID().toString(), Field.Store.YES));
//                w.addDocument(doc);
//
//                ISynset synset = word.getSynset();
//                List<IWord> synonyms = synset.getWords();
//                StringBuffer sb = new StringBuffer();
//                for (IWord syn : synonyms) {
//                    if (!syn.getID().equals(word.getID()))
//                        sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
//                }
//                doc.add(new StringField("synonymId", sb.toString(), Field.Store.YES));
//
//                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
//            }
//
//            w.close();
//
//        }
//
//        iterator = dict.getIndexWordIterator(POS.VERB);
//        for (; iterator.hasNext();) {
//
//            IIndexWord indexWord = iterator.next();
//            List<IWordID> wordIdds = indexWord.getWordIDs();
//
//            IndexWriterConfig config = new IndexWriterConfig(analyzer);
//            IndexWriter w = new IndexWriter(index, config);
//            for (IWordID wordId : wordIdds) {
//                Document doc = new Document();
//                doc.add(new TextField("word", wordId.getLemma(), Field.Store.YES));
//                doc.add(new StringField("pos", wordId.getPOS().toString(), Field.Store.YES));
//
//                IWord word = dict.getWord(wordId);
//                doc.add(new StringField("description", word.getSynset().getGloss(), Field.Store.YES));
//                doc.add(new StringField("id", word.getID().toString(), Field.Store.YES));
//                w.addDocument(doc);
//
//                ISynset synset = word.getSynset();
//                List<IWord> synonyms = synset.getWords();
//                StringBuffer sb = new StringBuffer();
//                for (IWord syn : synonyms) {
//                    if (!syn.getID().equals(word.getID()))
//                        sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
//                }
//                doc.add(new StringField("synonymId", sb.toString(), Field.Store.YES));
//
//                System.out.println(wordId.getPOS() + " " + wordId.getLemma());
//            }
//
//            w.close();
//
//        }

        posMap = new HashMap<String, POS>();
        posMap.put(edu.asu.conceptpower.core.POS.NOUN, POS.NOUN);
        posMap.put(edu.asu.conceptpower.core.POS.VERB, POS.VERB);
        posMap.put(edu.asu.conceptpower.core.POS.ADVERB, POS.ADVERB);
        posMap.put(edu.asu.conceptpower.core.POS.ADJECTIVE, POS.ADJECTIVE);

    }

    public ConceptEntry getConcept(String id) {
        ConceptEntry[] conceptEntry = luceneUtility.queryLuceneIndex(null, null, null, id);
        // Returning only the first occurence because id is a unique value in
        // the concept. So the array will contain only one record for each id
        return conceptEntry[0];
    }

    public ConceptEntry[] getSynonyms(String id) {
        IWordID wordId = null;
        try {
            wordId = WordID.parseWordID(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        List<ConceptEntry> entries = new ArrayList<ConceptEntry>();
        if (wordId != null) {
            IWord word = dict.getWord(wordId);
            ISynset synset = word.getSynset();
            if (synset != null) {
                List<IWord> synonyms = synset.getWords();
                for (IWord synonym : synonyms) {
                    entries.add(createConceptEntry(synonym));
                }
            }
        }

        return entries.toArray(new ConceptEntry[entries.size()]);
    }

    protected ConceptEntry createConceptEntry(IWord word) {
        ConceptEntry entry = new ConceptEntry();
        entry.setId(word.getID().toString());
        entry.setWordnetId(word.getID().toString());
        entry.setWord(word.getLemma());
        entry.setPos(word.getPOS().name());
        entry.setConceptList(Constants.WORDNET_DICTIONARY);

        entry.setDescription(word.getSynset().getGloss());

        ISynset synset = word.getSynset();
        List<IWord> synonyms = synset.getWords();
        StringBuffer sb = new StringBuffer();
        for (IWord syn : synonyms) {
            if (!syn.getID().equals(word.getID()))
                sb.append(syn.getID().toString() + edu.asu.conceptpower.core.Constants.SYNONYM_SEPARATOR);
        }
        entry.setSynonymIds(sb.toString());

        return entry;
    }

    protected ConceptEntry getConceptFromWordId(IWordID wordId) {
        IWord word = dict.getWord(wordId);
        ConceptEntry entry = null;
        if (word != null)
            entry = createConceptEntry(word);
        return entry;

    }

    public ConceptEntry[] getEntriesForWord(String word) {
        return luceneUtility.queryLuceneIndex(word, null, null, null);
    
    }

    public ConceptEntry[] getEntriesForWord(String word, String pos) {
        POS pPOS = posMap.get(pos);
        if (pPOS == null)
            return null;

        word = word.replace(" ", "");
        return luceneUtility.queryLuceneIndex(word, pos, null, null);
    }

    @PreDestroy
    public void close() {
        dict.close();
    }
    
    public String getLucenePath() {
        return lucenePath;
    }

    public void setLucenePath(String lucenePath) {
        this.lucenePath = lucenePath;
    }

}
