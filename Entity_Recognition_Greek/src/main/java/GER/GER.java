package GER;

import java.util.ArrayList;
import java.util.List;


import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MatchingType;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;

/*
 * 
 * @author Nikos Kontonasios
 */
public class GER {
	private String inputText;
	private List<SemanticAnnotation> bfyAnnotations_exact;
	private List<SemanticAnnotation> bfyAnnotations_partial;
	private List<SemanticAnnotation> bfyAnnotations_merged;
	private ArrayList<String[]> json;
	
	public GER() {
		this.inputText = "";
	}
	
	public GER(String inputText) {
		this.inputText = inputText;
	}
	
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	
	public String getInputText() {
		return this.inputText;
	}
	
	public ArrayList<String[]> getJson(){
		return this.json;
	}
	
	public void createJsonExact(){
		List<SemanticAnnotation> annotations = this.getAnnotationsExact();
		String[] annotation = new String[2];
		for(int i=0; i<annotations.size(); i++) {
			String frag = this.getInputText().substring(annotations.get(i).getCharOffsetFragment().getStart(),
				annotations.get(i).getCharOffsetFragment().getEnd() + 1);
			annotation[0] = frag;
			annotation[1] = annotations.get(i).getDBpediaURL();
			json.add(annotation.clone());
		}
	}
	
	public List<SemanticAnnotation> getAnnotationsExact(){
		return this.bfyAnnotations_exact;
	}
	
	public List<SemanticAnnotation> getAnnotationsPartial(){
		return this.bfyAnnotations_partial;
	}
	
	public List<SemanticAnnotation> getAnnotationsMerged(){
		return this.bfyAnnotations_merged;
	}
	
	public void findAnnotationsExact() {
		BabelfyParameters bp = new BabelfyParameters();
		bp.setMatchingType(MatchingType.EXACT_MATCHING);
		Babelfy bfy = new Babelfy(bp);
		this.bfyAnnotations_exact = bfy.babelfy(this.getInputText(), Language.EL);
		removeUnnecessaryAnnotations(this.bfyAnnotations_exact);
	}
	
	public void findAnnotationsPartial() {
		BabelfyParameters bp = new BabelfyParameters();
		bp.setMatchingType(MatchingType.PARTIAL_MATCHING);
		Babelfy bfy = new Babelfy(bp);
		this.bfyAnnotations_partial = bfy.babelfy(this.getInputText(), Language.EL);
		removeUnnecessaryAnnotations(this.bfyAnnotations_partial);
	}
	
	public void findAnnotationsMerged() {
		this.findAnnotationsExact();
		this.findAnnotationsPartial();
		this.bfyAnnotations_merged = this.mergeAnnotations(this.getAnnotationsExact(), this.getAnnotationsPartial());
		removeDuplicates(this.bfyAnnotations_merged);
		removeUnnecessaryAnnotations(this.bfyAnnotations_merged);
	}
	
	public void removeNoLinkEntities(List<SemanticAnnotation> annotations) {
		for(int i = 0; i < annotations.size(); i++) {
			if(annotations.get(i).getDBpediaURL() == null) {
				annotations.remove(i--);
			}
		}
	}
	
	public void removeDuplicates(List<SemanticAnnotation> annotations) {
		for(int i = 0; i < annotations.size()-1; i++) {
			SemanticAnnotation curr = annotations.get(i);
			SemanticAnnotation next = annotations.get(i+1);
			if(curr.getCharOffsetFragment().getStart() != next.getCharOffsetFragment().getStart()) continue;
			if(curr.getCharOffsetFragment().getEnd() != next.getCharOffsetFragment().getEnd()) continue;
			annotations.remove(i+1);
		}
	}
	
	public List<SemanticAnnotation> mergeAnnotations(List<SemanticAnnotation> annotations1, List<SemanticAnnotation> annotations2){
		List<SemanticAnnotation> merged = annotations1;
		
		int i = 0, j = 0;
		while (i < annotations1.size() && j < annotations2.size())
        {
            if (annotations1.get(i).getTokenOffsetFragment().getStart() <= annotations2.get(j).getTokenOffsetFragment().getStart())
                i++;
            else
            	merged.add(i, annotations2.get(j++));
        }
     
        // Store remaining elements of second array
        while (j < annotations2.size())
            merged.add(annotations2.get(j++));
		return merged;
	}
	
	public void printAnnotations(List<SemanticAnnotation> annotations) {
		System.out.println(annotations.size());
		for (SemanticAnnotation annotation : annotations)
		{
			//splitting the input text using the CharOffsetFragment start and end anchors
			String frag = this.getInputText().substring(annotation.getCharOffsetFragment().getStart(),
				annotation.getCharOffsetFragment().getEnd() + 1);
			System.out.println(frag + "\t" + annotation.getBabelSynsetID());
			System.out.println("\t" + annotation.getCharOffsetFragment());
			System.out.println("\t" + annotation.getTokenOffsetFragment());
			//System.out.println("\t" + annotation.getCoherenceScore());
			//System.out.println("\t" + annotation.getGlobalScore());
			//System.out.println("\t" + annotation.getScore());
			System.out.println("\t" + annotation.getBabelNetURL());
			System.out.println("\t" + annotation.getDBpediaURL());
			System.out.println("\t" + annotation.getSource());
		}
	}
	
	public void removeUnnecessaryAnnotations(List<SemanticAnnotation> annotations) {
		for(int i = 0; i < annotations.size(); i++) {
			SemanticAnnotation annotation = annotations.get(i);
			if(annotation.getTokenOffsetFragment().getStart() == annotation.getTokenOffsetFragment().getEnd()) {
				continue;
			}
			if(i < annotations.size()-1) {
				if(annotations.get(i+1).getTokenOffsetFragment().getEnd() == annotation.getTokenOffsetFragment().getEnd()) {
					annotations.remove(i+1);
				}
			}
			if(i > 0) {
				if(annotations.get(i-1).getTokenOffsetFragment().getStart() == annotation.getTokenOffsetFragment().getStart()) {
					annotations.remove(i-1);
				}
			}
		}
	}
}