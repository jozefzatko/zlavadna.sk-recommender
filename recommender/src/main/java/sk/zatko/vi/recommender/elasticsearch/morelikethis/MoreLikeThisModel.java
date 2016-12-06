package sk.zatko.vi.recommender.elasticsearch.morelikethis;

public class MoreLikeThisModel {

	private MoreLikeThisModelDetails more_like_this;
	
	public MoreLikeThisModel(MoreLikeThisModelDetails more_like_this) {

		this.more_like_this = more_like_this;
	}

	public MoreLikeThisModelDetails getMore_like_this() {
		return more_like_this;
	}

	public void setMore_like_this(MoreLikeThisModelDetails more_like_this) {
		this.more_like_this = more_like_this;
	}

}
