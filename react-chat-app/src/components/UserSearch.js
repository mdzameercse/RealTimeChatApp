import React, { useState } from "react";
import api from "../api";

export default function UserSearch() {
  const [users, setUsers] = useState([]);
  const [query, setQuery] = useState("");

  const searchUsers = () => {
    api.get("/user/showallusers").then((res) => setUsers(res.data));
  };

  const addFriend = (id) => {
    api.post(`/friend/addfriend/${id}`).then(() => alert("Friend added!"));
  };

  return (
    <div className="p-2 border-b">
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search users"
        className="w-full p-2 border rounded"
      />
      <button
        className="bg-blue-500 text-white px-4 py-1 rounded mt-2 w-full"
        onClick={searchUsers}
      >
        Search
      </button>
      <div>
        {users
          .filter((u) => u.username.includes(query) && query !== "")
          .map((u) => (
            <div key={u.id} className="flex justify-between p-2">
              {u.username}
              <button
                className="bg-green-500 text-white px-2 rounded"
                onClick={() => addFriend(u.id)}
              >
                Add
              </button>
            </div>
          ))}
      </div>
    </div>
  );
}
