package edu.asu.conceptpower.lucene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.conceptpower.exceptions.LuceneException;

@Service
public class LuceneIndexManager implements ILuceneIndexManger {

    @Autowired
    private ILuceneUtility luceneUtility;
    
    public void deleteIndexes() throws LuceneException {
        luceneUtility.deleteIndexes();
    }

    public void indexConcepts() throws LuceneException {
        luceneUtility.indexConcepts();
    }
}
