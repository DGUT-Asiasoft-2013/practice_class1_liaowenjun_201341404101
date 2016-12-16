package utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page <T>{
	List <T> content;
	Integer number;
	
	public List <T> getContent() {
		return content;
	}
	public void setContent(List <T> content) {
		this.content = content;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Article get(int position) {
		return (Article) content.get(position);
	}
	public int size() {
		return content.size();
	}
	public void addAll(List<T> content2) {
		setContent(content2);
		
	}
	
}
