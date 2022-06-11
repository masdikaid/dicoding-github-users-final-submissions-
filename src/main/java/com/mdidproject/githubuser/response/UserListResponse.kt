import com.google.gson.annotations.SerializedName
import com.mdidproject.githubuser.response.UserItem

data class UserListResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val users: List<UserItem>
)

