import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: ConfigurationTest
 *
 */
public class Editor extends HttpServlet {
	/**
	 * The Servlet constructor
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */

	Connection con;
	PostService postService;
	List<String> validActions;

	public Editor() {
	}

	public void init() throws ServletException {
		/* write any servlet initialization code here or remove this function */
		postService = new PostService();
		validActions = new ArrayList<String>(Arrays.asList("open", "list", "delete", "save", "preview"));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		/* write any servlet cleanup code here or remove this function */
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * Handles HTTP GET requests
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null || action.isEmpty()) {
			request.getRequestDispatcher("/list.jsp").forward(request, response);
		}
		// Check if the action is valid or not
		else if (!validActions.contains(action)) {
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");

			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);

			request.setAttribute("userAction", action);
			request.setAttribute("errorMsg", Constants.INVALID_ACTION);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.getRequestDispatcher("/error.jsp").forward(request, response);

		}

		// Action:: OPEN
		if (action.equalsIgnoreCase("open")) {
			String closepreviewflag = request.getParameter("closepreviewflag");
			String newOpenFlag = request.getParameter("newOpenFlag");
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");

			if (title == null || title.isEmpty()) {
				title = (String) request.getAttribute("title");
			}
			if (body == null || body.isEmpty()) {
				body = (String) request.getAttribute("body");
			}
			if (username == null || username.isEmpty()) {
				username = (String) request.getAttribute("username");
			}
			if (postId == null || postId.isEmpty()) {
				postId = (String) request.getAttribute("postid");
			}

			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);

			if (username == null || username.isEmpty() || postId == null || postId.isEmpty()) {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else if ((closepreviewflag != null && closepreviewflag.equals("true"))
					|| (title != null && body != null) || (postId.equals("0"))) {
				request.getRequestDispatcher("/edit.jsp").forward(request, response);
			} else {
				Post resultPost = postService.getPostContent(username, postId, con);
				if (resultPost != null) {
					request.setAttribute("title", resultPost.getTitle());
					request.setAttribute("body", resultPost.getBody());
					request.getRequestDispatcher("/edit.jsp").forward(request, response);
				} else if (newOpenFlag != null && newOpenFlag.equals("true")) {
					request.getRequestDispatcher("/edit.jsp").forward(request, response);
				} else {
					request.setAttribute("userAction", action);
					request.setAttribute("errorMsg", Constants.NOT_FOUND);
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}

			}
		}

		// Action:: LIST
		if (action.equalsIgnoreCase("list")) {
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);
			if (username != null && !username.isEmpty()) {
				List<Post> listOfPosts = postService.getListOfPosts(username, con);
				request.setAttribute("listOfPosts", listOfPosts);
				request.getRequestDispatcher("/list.jsp").forward(request, response);
			} else {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			}

		}

		// Action:: DELETE||SAVE
		if (action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("save")) {
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			request.setAttribute("title", title);
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("body", body);
			request.setAttribute("userAction", action);
			request.setAttribute("errorMsg", Constants.METHOD_ERROR);

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}

		// Action:: PREVIEW
		if (action.equalsIgnoreCase("preview")) {
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			request.setAttribute("title", title);
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("body", body);
			if (username != null && !username.isEmpty() && postId != null && !postId.isEmpty()) {
				String parsedText = postService.previewPost(username, postId, title, body, con);
				request.setAttribute("parsedText", parsedText);
				if (title == null) {
					request.setAttribute("title", "");
				}
				request.getRequestDispatcher("/preview.jsp").forward(request, response);

			} else {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				request.getRequestDispatcher("/error.jsp").forward(request, response);

			}
		}
	}

	/**
	 * Handles HTTP POST requests
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null || action.isEmpty()) {
			request.getRequestDispatcher("/list.jsp").forward(request, response);
		}
		// Check if the action is valid or not
		else if (!validActions.contains(action)) {
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");

			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);

			request.setAttribute("userAction", action);
			request.setAttribute("errorMsg", Constants.INVALID_ACTION);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			request.getRequestDispatcher("/error.jsp").forward(request, response);

		}

		// Action::OPEN
		if (action.equalsIgnoreCase("open")) {
			String closepreviewflag = request.getParameter("closepreviewflag");
			String newOpenFlag = request.getParameter("newOpenFlag");
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");

			if (title == null || title.isEmpty()) {
				title = (String) request.getAttribute("title");
			}
			if (body == null || body.isEmpty()) {
				body = (String) request.getAttribute("body");
			}
			if (username == null || username.isEmpty()) {
				username = (String) request.getAttribute("username");
			}
			if (postId == null || postId.isEmpty()) {
				postId = (String) request.getAttribute("postid");
			}

			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);
			if (username == null || username.isEmpty() || postId == null || postId.isEmpty()) {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			} else if ((closepreviewflag != null && closepreviewflag.equals("true"))
					|| (title != null && body != null) || (postId.equals("0"))) {
				request.getRequestDispatcher("/edit.jsp").forward(request, response);
			} else {
				Post resultPost = postService.getPostContent(username, postId, con);
				// equest.setAttribute("username", username);
				// request.setAttribute("postid", postId);

				if (resultPost != null) {
					request.setAttribute("title", resultPost.getTitle());
					request.setAttribute("body", resultPost.getBody());
					request.getRequestDispatcher("/edit.jsp").forward(request, response);
				} else if (newOpenFlag != null && newOpenFlag.equals("true")) {
					request.getRequestDispatcher("/edit.jsp").forward(request, response);
				} else {
					request.setAttribute("userAction", action);
					request.setAttribute("errorMsg", Constants.NOT_FOUND);
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}

			}
		}

		// Action:: LIST
		if (action.equalsIgnoreCase("list")) {
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);
			if (username != null && !username.isEmpty()) {
				List<Post> listOfPosts = postService.getListOfPosts(username, con);
				request.setAttribute("listOfPosts", listOfPosts);
				request.getRequestDispatcher("/list.jsp").forward(request, response);
			} else {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			}

		}

		// Actio:: SAVE
		if (action.equalsIgnoreCase("save")) {
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("title", title);
			request.setAttribute("body", body);
			if (username != null && !username.isEmpty() && postId != null && !postId.isEmpty() && title != null
					&& !title.isEmpty() && body != null && !body.isEmpty()) {
				postService.savePost(username, postId, title, body, con);
				List<Post> listOfPosts = postService.getListOfPosts(username, con);
				request.setAttribute("listOfPosts", listOfPosts);
				request.getRequestDispatcher("/list.jsp").forward(request, response);

			} else {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);

			}
		}

		// Action:: DELETE
		if (action.equalsIgnoreCase("delete")) {
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			request.setAttribute("title", title);
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("body", body);

			if (username != null && !username.isEmpty() && postId != null && !postId.isEmpty()) {
				int returnVal = postService.deletePost(username, postId, con);
				if (returnVal > 0) {
					List<Post> listOfPosts = postService.getListOfPosts(username, con);
					request.setAttribute("listOfPosts", listOfPosts);
					request.getRequestDispatcher("/list.jsp").forward(request, response);
				} else {
					request.setAttribute("userAction", action);
					request.setAttribute("errorMsg", Constants.NOT_FOUND);
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					request.getRequestDispatcher("/error.jsp").forward(request, response);
				}

			} else {

				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
			}
		}

		// Action:: PREVIEW
		if (action.equalsIgnoreCase("preview")) {
			String username = request.getParameter("username");
			String postId = request.getParameter("postid");
			String title = request.getParameter("title");
			String body = request.getParameter("body");

			request.setAttribute("title", title);
			request.setAttribute("username", username);
			request.setAttribute("postid", postId);
			request.setAttribute("body", body);
			if (username != null && !username.isEmpty() && postId != null && !postId.isEmpty()) {
				String parsedText = postService.previewPost(username, postId, title, body, con);
				request.setAttribute("parsedText", parsedText);
				request.getRequestDispatcher("/preview.jsp").forward(request, response);

			} else {
				request.setAttribute("userAction", action);
				request.setAttribute("errorMsg", Constants.PARAMETER_ERROR);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				request.getRequestDispatcher("/error.jsp").forward(request, response);

			}
		}
	}
}
