import React from "react";
import FriendList from "./FriendList";
import UserSearch from "./UserSearch";

export default function Sidebar({ friends, onSelectFriend }) {
  return (
    <div className="w-1/4 bg-gray-100 border-r flex flex-col">
      <div className="p-4 text-xl font-bold border-b">Chat App</div>
      <UserSearch />
      <FriendList friends={friends} onSelectFriend={onSelectFriend} />
    </div>
  );
}
