package edu.asu.conceptpower.lucene;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.wordnet.Constants;

@Component
public class LuceneUtility implements ILuceneUtility {

    @Autowired
    private WhitespaceAnalyzer whiteSpaceAnalyzer;

    @Autowired
    private StandardAnalyzer standradAnalyzer;

    public ConceptEntry queryById(String id) {
        Query q;
        ConceptEntry entry = null;
        Directory index = null;
        String lucenePath = System.getProperty("lucenePath");
        try {
            q = new QueryParser("id", whiteSpaceAnalyzer).parse("id:" + id);
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            index = FSDirectory.open(relativePath);

            int hitsPerPage = 10;
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                entry = getConceptFromDocument(d);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entry;
    }

    public List<ConceptEntry> queryByListName(String listName) {
        IndexReader reader = null;
        List<ConceptEntry> conceptEntryList = new ArrayList<ConceptEntry>();
        try {
            Query q = new QueryParser("listName", whiteSpaceAnalyzer).parse("listName:" + listName);
            String lucenePath = System.getProperty("lucenePath");
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, 10);
            ScoreDoc[] hits = docs.scoreDocs;

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ConceptEntry entry = getConceptFromDocument(d);
                conceptEntryList.add(entry);
            }
        } catch (Exception ex) {
            // TOOD
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {

            }
        }

        return conceptEntryList;

    }

    public void deleteById(String id) {
        IndexWriter writer = null;
        try {
            Query q = new QueryParser("id", whiteSpaceAnalyzer).parse("id:" + id);
            String lucenePath = System.getProperty("lucenePath");
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            IndexWriterConfig configWhiteSpace = new IndexWriterConfig(whiteSpaceAnalyzer);
            writer = new IndexWriter(index, configWhiteSpace);
            writer.deleteDocuments(q);
        } catch (Exception ex) {

        } finally {
            try {
                writer.close();
            } catch (Exception ex) {

            }
        }

    }

    public void insertConcept(ConceptEntry entry) {
        IndexWriter writer = null;
        try {
            String lucenePath = System.getProperty("lucenePath");
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);
            IndexWriterConfig config = new IndexWriterConfig(standradAnalyzer);
            writer = new IndexWriter(index, config);
            Document doc = new Document();
            doc.add(new TextField("word", entry.getWord().replace(" ", ""), Field.Store.YES));
            doc.add(new StringField("pos", entry.getPos().toString(), Field.Store.YES));

            doc.add(new StringField("description", entry.getDescription() != null ? entry.getDescription() : "",
                    Field.Store.YES));
            doc.add(new StringField("id", entry.getId(), Field.Store.YES));
            doc.add(new StringField("listName", entry.getConceptList() != null ? entry.getConceptList() : "",
                    Field.Store.YES));

            doc.add(new StringField("synonymId", entry.getSynonymIds() != null ? entry.getSynonymIds() : "",
                    Field.Store.YES));
            doc.add(new StringField("equalTo", entry.getEqualTo() != null ? entry.getEqualTo() : "", Field.Store.YES));
            doc.add(new StringField("similar", entry.getSimilarTo() != null ? entry.getSimilarTo() : "",
                    Field.Store.YES));
            doc.add(new StringField("types", entry.getTypeId() != null ? entry.getTypeId() : "", Field.Store.YES));
            doc.add(new StringField("creatorId", entry.getCreatorId() != null ? entry.getCreatorId() : "",
                    Field.Store.YES));
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            doc.add(new StringField("modifiedTime", formatter.format(cal.getTime()), Field.Store.YES));

            writer.addDocument(doc);
            writer.commit();
        } catch (Exception ex) {

        } finally {
            try {
                writer.close();
            } catch (Exception ex) {

            }
        }

    }

    public ConceptEntry[] queryByWordPos(String word, String pos) {
        IndexReader reader = null;
        List<ConceptEntry> concepts = new ArrayList<ConceptEntry>();
        try {
            Query q = null;
            if (pos != null) {
                q = new QueryParser("word", standradAnalyzer).parse("word:" + word + " AND pos:" + pos);
            } else {
                q = new QueryParser("word", standradAnalyzer).parse("word:" + word);
            }
            String lucenePath = System.getProperty("lucenePath");
            Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
            Directory index = FSDirectory.open(relativePath);

            int hitsPerPage = 10;

            reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                ConceptEntry entry = getConceptFromDocument(d);
                concepts.add(entry);
            }
        } catch (Exception ex) {

        } finally {
            try {
                reader.close();
            } catch (Exception ex) {

            }
        }
        return concepts.toArray(new ConceptEntry[concepts.size()]);
    }

    private ConceptEntry getConceptFromDocument(Document d) {
        ConceptEntry entry = new ConceptEntry();
        entry.setId(d.get("id"));
        entry.setWord(d.get("word"));
        entry.setPos(d.get("pos"));
        entry.setConceptList(Constants.WORDNET_DICTIONARY);
        entry.setDescription(d.get("description"));
        entry.setWordnetId(d.get("id"));
        entry.setSynonymIds(d.get("synonymId"));
        entry.setConceptList(d.get("listName"));
        entry.setTypeId(d.get("types"));
        entry.setEqualTo(d.get("equalTo"));
        entry.setSimilarTo(d.get("similar"));
        entry.setModified(d.get("creatorId"));
        entry.setSynonymIds(d.get("synonymId"));
        entry.setCreatorId(d.get("creatorId"));
        return entry;
    }

}
