import java.sql.Connection;
import java.util.List;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class PostService {
	private PostDao postDao = new PostDao();

	public PostService() {
	}

	public Post getPostContent(String username, String postId, Connection con) {
		Post resultPost = null;

		if (username != null && !username.isEmpty() && postId != null && !postId.isEmpty()) {
			Long id = Long.parseLong(postId);
			resultPost = postDao.getPostContent(username, id, con);
		}
		return resultPost;
	}

	public List<Post> getListOfPosts(String username, Connection con) {
		List<Post> listOfPosts = null;
		listOfPosts = postDao.getListOfPosts(username, con);
		return listOfPosts;
	}

	public void savePost(String username, String postId, String title, String body, Connection con) {
		if (postId != null && !postId.isEmpty()) {
			Long id = Long.parseLong(postId);
			System.out.println("INSIDE SAVE POST SERICE");
			Post post = postDao.findPost(username, id, con);
			System.out.println("CAME BACK SAVE POST SERICE");

			if(post != null){
				postDao.updatePost(username, title, body, id, con);
			}
			else{
				postDao.insertPost(username, title, body, id, con);
			}		
		}

	}

	public int deletePost(String username, String postId, Connection con) {
		Long id = null;
		int returnVal = 0;
		if (postId != null && !postId.isEmpty()) {
			id = Long.parseLong(postId);
		}

		if (id != null) {
			returnVal = postDao.deletePost(username, id, con);
		}
		return returnVal;
	}
	
	public String previewPost(String username, String postId, String title, String body, Connection con) {
		Parser parser = Parser.builder().build();
		if(body != null){
			Node document = parser.parse(body);
			HtmlRenderer renderer = HtmlRenderer.builder().build();	
			String html = renderer.render(document);
			return html;
		}
		return "";
		
	}
}
