import React from "react";

export default function FriendList({ friends, onSelectFriend }) {
  return (
    <div className="flex-1 overflow-y-auto">
      {friends.map((f, index) => (
        <div
          key={index}
          className="p-4 hover:bg-gray-200 cursor-pointer border-b"
          onClick={() => onSelectFriend(f.friend)}
        >
          {f.friend.username}
        </div>
      ))}
    </div>
  );
}
