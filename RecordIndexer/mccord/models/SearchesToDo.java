package models;

public class SearchesToDo {
	private Integer fieldId;
	private String searchValue;
	
	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof SearchesToDo){
			SearchesToDo r = (SearchesToDo)o;
			return (r.getFieldId() == this.getFieldId() && r.getSearchValue().equals(this.getSearchValue()));
		}
		return false;
	}
	
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldId;
        result = prime * result + ((searchValue == null) ? 0 : searchValue.hashCode());
        return result;
    }
}
