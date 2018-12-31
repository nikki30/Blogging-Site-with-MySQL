import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

	public PostDao() {
	}

	public Post getPostContent(String username, Long postId, Connection con) {
		Post resultPost = null;
		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement("SELECT * FROM Posts WHERE username = ? and postid = ?");
			preparedStmt.setString(1, username);
			preparedStmt.setLong(2, postId);
			ResultSet resultSet = preparedStmt.executeQuery();
			while (resultSet.next()) {
				resultPost = new Post();
				resultPost.setUsername(resultSet.getString("username"));
				resultPost.setPostId(resultSet.getLong("postid"));
				resultPost.setTitle(resultSet.getString("title"));
				resultPost.setBody(resultSet.getString("body"));
				resultPost.setCreatedDate(resultSet.getString("created"));
				resultPost.setModifiedDate(resultSet.getString("modified"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultPost;
	}

	public List<Post> getListOfPosts(String username, Connection con) {
		List<Post> listOfPosts = new ArrayList<Post>();
		try {
			PreparedStatement preparedStmt = con.prepareStatement("SELECT * FROM Posts WHERE username = ?");
			preparedStmt.setString(1, username);
			System.out.println("#############"+preparedStmt);
			ResultSet resultSet = preparedStmt.executeQuery();
			
			while (resultSet.next()) {
				Post post = new Post();
				post.setUsername(resultSet.getString("username"));
				post.setPostId(resultSet.getLong("postid"));
				post.setTitle(resultSet.getString("title"));
				post.setBody(resultSet.getString("body"));
				post.setCreatedDate(resultSet.getString("created"));
				post.setModifiedDate(resultSet.getString("modified"));
				listOfPosts.add(post);
			}

			if(listOfPosts.size() <= 0){
				listOfPosts = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfPosts;
	}

	public void insertPost(String username, String title, String body, Long postId, Connection con) {
		try {
			System.out.println("INSIDE INSERT POST DAO");
			Long id;
			Long idFromTable = getNextPostId(username, con);
			if(postId != null && postId > 0){
				id = postId;
			}
			else{
				id = idFromTable;
			}
			PreparedStatement preparedStmt = con.prepareStatement("INSERT INTO Posts(username, postid, title, body) VALUES (?,?,?,?)");
			preparedStmt.setString(1, username);
			preparedStmt.setLong(2, id);
			preparedStmt.setString(3, title);
			preparedStmt.setString(4, body);

			preparedStmt.executeUpdate();
			System.out.println("edited save-------------------------------"+preparedStmt);

			Long greater = (id > idFromTable) ?id : idFromTable;
			System.out.println("GREATER-------------------------------"+greater);
			updatePostId(username, con, greater);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public void updatePost(String username, String title, String body, Long postId, Connection con) {
		try {
			System.out.println("INSIDE upDATE POST DAO");
			PreparedStatement preparedStmt = con.prepareStatement("UPDATE Posts SET title = ?, body = ? WHERE username = ? AND  postid = ?");
			preparedStmt.setString(1, title);
			preparedStmt.setString(2, body);
			preparedStmt.setString(3, username);
			preparedStmt.setLong(4, postId);

			preparedStmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Long getNextPostId(String username, Connection con) {
		Long nextId = null;
		try {
			PreparedStatement preparedStmt = con.prepareStatement("SELECT lastId from PostIDMap where username = ?");
			preparedStmt.setString(1, username);
			ResultSet resultSet = preparedStmt.executeQuery();

			while (resultSet.next()) {
				nextId = resultSet.getLong("lastId");
				if (nextId != null) {
					nextId += 1;
				}
			}

			if (nextId == null) {
				preparedStmt = con.prepareStatement("INSERT INTO PostIDMap values (?, ?)");
				preparedStmt.setString(1, username);
				preparedStmt.setLong(2, 1L);
				preparedStmt.executeUpdate();
				nextId = 1L;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nextId;
	}

	public void updatePostId(String username, Connection con, Long postId) {
		try {
			PreparedStatement preparedStmt = con.prepareStatement("UPDATE PostIDMap SET lastId = ? WHERE username = ?");
			preparedStmt.setLong(1, postId);
			preparedStmt.setString(2, username);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int deletePost(String username, Long postId,  Connection con) {
		int returnVal = 0;
		try {
			PreparedStatement preparedStmt = con.prepareStatement("DELETE FROM Posts WHERE username = ? AND postid = ?");
			preparedStmt.setString(1, username);
			preparedStmt.setLong(2, postId);
			returnVal = preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return returnVal;
	}

		public Post findPost(String username, Long postId,  Connection con) {
		Post post = null;
		try {
			System.out.println("INSIDE fiND POST DAO");
			PreparedStatement preparedStmt = con.prepareStatement("SELECT * FROM Posts WHERE username = ? AND postid = ?");
			preparedStmt.setString(1, username);
			preparedStmt.setLong(2, postId);
			ResultSet resultSet = preparedStmt.executeQuery();
			while (resultSet.next()) {
				post = new Post();
				post.setUsername(resultSet.getString("username"));
				post.setPostId(resultSet.getLong("postid"));
				post.setTitle(resultSet.getString("title"));
				post.setBody(resultSet.getString("body"));
				post.setCreatedDate(resultSet.getString("created"));
				post.setModifiedDate(resultSet.getString("modified"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}
}
