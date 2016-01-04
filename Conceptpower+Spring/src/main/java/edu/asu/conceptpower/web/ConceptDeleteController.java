package edu.asu.conceptpower.web;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.conceptpower.core.ConceptEntry;
import edu.asu.conceptpower.core.IConceptManager;
import edu.asu.conceptpower.wordnet.WordNetManager;
import edu.asu.conceptpower.wrapper.ConceptEntryWrapper;
import edu.asu.conceptpower.wrapper.impl.ConceptEntryWrapperCreator;

/**
 * This class provides all the required methods for deleting a concept
 * 
 * @author Chetan
 * 
 */
@Controller
public class ConceptDeleteController {

	@Autowired
	private IConceptManager conceptManager;

	@Autowired
	private ConceptEntryWrapperCreator wrapperCreator;
	
	@Autowired
    private WhitespaceAnalyzer  whiteSpaceAnalyzer;
	
	@Autowired
	private WordNetManager wordNetManager;
	
	Directory index;
	
	/**
	 * This method provides details of a concept to be deleted for concept
	 * delete page
	 * 
	 * @param conceptid
	 *            ID of a concept to be deleted
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to concept delete page
	 */
	@RequestMapping(value = "auth/conceptlist/deleteconcept/{conceptid}", method = RequestMethod.GET)
	public String prepareDeleteConcept(@PathVariable("conceptid") String conceptid, ModelMap model,@RequestParam(value = "fromHomeScreenDelete",required=false)String fromHomeScreenDelete) {
		ConceptEntry concept = conceptManager.getConceptEntry(conceptid);
		model.addAttribute("word", concept.getWord());
		model.addAttribute("description", concept.getDescription());
		model.addAttribute("conceptId", concept.getId());
		model.addAttribute("wordnetId", concept.getWordnetId());
		model.addAttribute("pos", concept.getPos());
		model.addAttribute("conceptList", concept.getConceptList());
		model.addAttribute("type", concept.getTypeId());
		model.addAttribute("equal", concept.getEqualTo());
		model.addAttribute("similar", concept.getSimilarTo());
		model.addAttribute("user", concept.getModified());
		model.addAttribute("modified", concept.getModified());
		model.addAttribute("synonyms", concept.getSynonymIds());
	    
	    //conceptManager.deleteConcept(conceptid);
	    
		if(fromHomeScreenDelete!=null){
		model.addAttribute("fromHomeScreenDelete",fromHomeScreenDelete);
		}
		else{
		    model.addAttribute("fromHomeScreenDelete",false);
		}
		return "/auth/conceptlist/deleteconcept";
	}

	/**
	 * This method returns user to a particular concept list page after the user
	 * cancels concept delete operation
	 * 
	 * @param conceptList
	 *            concept list name where user has to redirected
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a particular concept list
	 */
	@RequestMapping(value = "auth/concepts/canceldelete/{conceptList}", method = RequestMethod.GET)
	public String cancelDelete(@PathVariable("conceptList") String conceptList,
			ModelMap model) {
		List<ConceptEntry> founds = conceptManager
				.getConceptListEntries(conceptList);

		List<ConceptEntryWrapper> foundConcepts = wrapperCreator
				.createWrappers(founds != null ? founds
						.toArray(new ConceptEntry[founds.size()])
						: new ConceptEntry[0]);

		model.addAttribute("result", foundConcepts);
		return "/auth/conceptlist/concepts";
	}

	/**
	 * This method deletes a concept
	 * 
	 * @param id
	 *            Concept ID to be deleted
	 * @param model
	 *            A generic model holder for Servlet
	 * @return String value to redirect user to a particular concept list page
	 */
	@RequestMapping(value = "auth/conceptlist/deleteconceptconfirm/{id}", method = RequestMethod.GET)
    public String confirmlDelete(@PathVariable("id") String id,
            @RequestParam(value = "fromHomeScreenDelete") String fromHomeScreenDelete, ModelMap model) {
       
	    
	    ConceptEntry concept = wordNetManager.getConcept(id);
	    IndexWriter writer = null;
	    try{
	    Query q = new QueryParser("word", whiteSpaceAnalyzer).parse("id:" + id);
	    String lucenePath = System.getProperty("lucenePath");
	    Path relativePath = FileSystems.getDefault().getPath(lucenePath, "index");
	    index = FSDirectory.open(relativePath);
	    IndexWriterConfig config = new IndexWriterConfig(whiteSpaceAnalyzer);
	    writer = new IndexWriter(index, config);
	    writer.deleteDocuments(q);
	    }
	    catch(Exception ex){
	        //TODO
	    }
	    finally{
	        try{
	            writer.close();
	        }
	        catch(Exception ex){
	            //TODO
	        }
	    }
	    

        List<ConceptEntry> founds = conceptManager.getConceptListEntries(concept.getConceptList());

        List<ConceptEntryWrapper> foundConcepts = wrapperCreator
                .createWrappers(founds != null ? founds.toArray(new ConceptEntry[founds.size()]) : new ConceptEntry[0]);

        model.addAttribute("result", foundConcepts);
        if (fromHomeScreenDelete.equalsIgnoreCase("true")) {
            return "redirect:/login";
        }
        return "/auth/conceptlist/concepts";
    }
	
    @RequestMapping(value = "auth/conceptlist/deleteconcepts/{id}", method = RequestMethod.GET)
    public String deleteConcept(@PathVariable("id") String id, ModelMap model,@ModelAttribute("conceptSearchBean")ConceptSearchBean conceptSearchBean) {
        conceptManager.deleteConcept(id);
        return "welcome";
    }
	
}
