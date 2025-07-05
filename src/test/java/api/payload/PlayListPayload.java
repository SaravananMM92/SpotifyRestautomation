package api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayListPayload {

	String name;
	String description;
	
	@JsonProperty("public")
    Boolean isPublic;
	String collaborative;
	
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getCollaborative() {
		return collaborative;
	}
	public void setCollaborative(String collaborative) {
		this.collaborative = collaborative;
	}
	
}
