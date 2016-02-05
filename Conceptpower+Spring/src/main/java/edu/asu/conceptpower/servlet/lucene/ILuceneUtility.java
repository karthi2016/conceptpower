package edu.asu.conceptpower.servlet.lucene;

import java.util.Map;

import edu.asu.conceptpower.servlet.core.ConceptEntry;
import edu.asu.conceptpower.servlet.exceptions.LuceneException;

public interface ILuceneUtility {

    public ConceptEntry[] queryLuceneIndex(String word, String pos, String listName, String id,String conceptType) throws LuceneException;

    public void deleteById(String id)throws LuceneException;

    public void insertConcept(ConceptEntry entry)throws LuceneException;
    
    public void deleteIndexes() throws LuceneException;
    
    public void indexConcepts() throws LuceneException;
    
    public ConceptEntry[] queryIndex(Map<String, String> fieldMap,String operator) throws LuceneException;
    
}