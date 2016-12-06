package sk.zatko.vi.recommender.elasticsearch.morelikethis;

public class LikeModel {

	private String _index;
	private String _type;
	private String _id;
	
	public LikeModel(String _index, String _type, String _id) {

		this._index = _index;
		this._type = _type;
		this._id = _id;
	}

	public String get_index() {
		return _index;
	}
	public void set_index(String _index) {
		this._index = _index;
	}

	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
}
