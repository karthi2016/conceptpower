package edu.asu.conceptpower.app.lucene;

import java.util.Map;

import edu.asu.conceptpower.app.exceptions.LuceneException;
import edu.asu.conceptpower.app.util.CCPSort;
import edu.asu.conceptpower.core.ConceptEntry;

public interface ILuceneUtility {

    public void deleteById(String id, String userName) throws LuceneException;

    public void insertConcept(ConceptEntry entry, String userName) throws LuceneException, IllegalAccessException;

    public void deleteIndexes(String userName) throws LuceneException;

    public void indexConcepts(String userName) throws LuceneException, IllegalArgumentException, IllegalAccessException;

    public ConceptEntry[] queryIndex(Map<String, String> fieldMap, String operator, int pageNumber,
            int numberOfRecordsPerPage, CCPSort ccpSort)
                    throws LuceneException, IllegalAccessException;

}