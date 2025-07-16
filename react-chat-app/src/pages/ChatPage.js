import React, { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import api from "../api";
import { useNavigate } from "react-router-dom";

  const handleAuthError = useCallback((err) => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem("currentUser");
      sessionStorage.removeItem("selectedFriend");
      navigate("/login");
    } else {
      console.error("Error:", err);
    }
  }, [navigate]);
export default function ChatPage() {
  const [friends, setFriends] = useState([]);
  const [currentUser, setCurrentUser] = useState(
  sessionStorage.getItem("currentUser") || null
);

  const [selectedFriend, setSelectedFriend] = useState(
    sessionStorage.getItem("selectedFriend")
      ? JSON.parse(sessionStorage.getItem("selectedFriend"))
      : null
  );
  const [allUsers, setAllUsers] = useState([]);
  const [showAddFriend, setShowAddFriend] = useState(false);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);
  const messagesEndRef = useRef(null);
  const stompClientRef = useRef(null);
  const [status, setStatus] = useState(null);
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
   const handleAuthError = (err) => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem("currentUser");
      sessionStorage.removeItem("selectedFriend");
      navigate("/login");
    } else {
      console.error("Error:", err);
    }
  };
// ✅ Filtered friends list
const filteredFriends = friends.filter((f) =>
  f.friend?.username?.toLowerCase().includes(searchTerm.toLowerCase())
);

  // ✅ Fetch online status
  useEffect(() => {
    if (selectedFriend) {
      const fetchStatus = () => {
        api.get(`/user/status/${selectedFriend.friend.username}`)
          .then((res) => setStatus(res.data))
          .catch((err) => handleAuthError(err));
      };
      fetchStatus();
      const interval = setInterval(fetchStatus, 10000);
      return () => clearInterval(interval);
    }
  }, [selectedFriend,handleAuthError]);

  // ✅ Detect screen size
  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // ✅ Fetch current user
  useEffect(() => {
    if (!currentUser) {
      api.get("/user/me")
        .then((res) => {
          setCurrentUser(res.data.username);
             sessionStorage.setItem("currentUser", res.data.username);
        })
        .catch((err) => handleAuthError(err));
    }
  }, [currentUser,handleAuthError]);

  // ✅ WebSocket connection
  useEffect(() => {
    if (!currentUser) return;
    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      reconnectDelay: 5000,
      debug: (str) => console.log(str),
    });
  
    client.onConnect = () => {
          console.log("✅ Connected to WebSocket");
      client.subscribe(`/topic/chat/${currentUser}`, (message) => {
        const msg = JSON.parse(message.body);
        if (
          selectedFriend &&
          (msg.senderUsername === selectedFriend.friend.username ||
            msg.receiverUsername === selectedFriend.friend.username)
        ) {
          setMessages((prev) => [...prev, msg]);
        }
      });
    };

    client.activate();
    stompClientRef.current = client;

    return () => {
      if (stompClientRef.current) stompClientRef.current.deactivate();
    };
  }, [currentUser, selectedFriend]);

  // ✅ Fetch friends list
 useEffect(() => {
  api.get("/friend/showall")
    .then((res) => setFriends(Array.isArray(res.data) ? res.data : []))
    .catch((err) => handleAuthError(err));
}, [handleAuthError]);

  // ✅ Fetch all users when Add Friend modal opens
  useEffect(() => {
    if (showAddFriend) {
      api.get("/user/showallusers")
        .then((res) => setAllUsers(res.data))
         .catch((err) => {
        console.error(err);
        setAllUsers([]);  // ✅ Prevent map error
      });
    }
  }, [showAddFriend,handleAuthError]);

  // ✅ Fetch messages for selected friend
  useEffect(() => {
    if (selectedFriend) {
      api.get(`/message/showchat/${selectedFriend.friend.id}`)
        .then((res) => setMessages(res.data))
        .catch((err) => handleAuthError(err));
    }
  }, [selectedFriend,handleAuthError]);

  // ✅ Auto-scroll
  useEffect(() => {
    const timeout = setTimeout(() => {
      if (messagesEndRef.current) {
        messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
      }
    }, 100);
    return () => clearTimeout(timeout);
  }, [messages]);

  // ✅ Error handler for auth


  const handleSelectFriend = (friend) => {
    setSelectedFriend(friend);
    sessionStorage.setItem("selectedFriend", JSON.stringify(friend));
  };

  const handleAddFriend = async (fid) => {
    try {
      await api.post(`/friend/addfriend/${fid}`);
      const addedFriend = allUsers.find((user) => user.id === fid);
      if (!addedFriend) return;
      setFriends((prev) => [...prev, { friend: addedFriend }]);
      setShowAddFriend(false);
      setAllUsers((prev) => prev.filter((user) => user.id !== fid));
    } catch (err) {
      handleAuthError(err);
    }
  };

  const handleSendMessage = () => {
    if (!newMessage.trim()) return;
    api
      .post(`/message/sendto/${selectedFriend.friend.id}`, newMessage, {
        headers: { "Content-Type": "text/plain" },
      })
      .then(() => {
        const msg = {
          content: newMessage,
          timestamp: new Date().toISOString(),
          senderUsername: currentUser,
          receiverUsername: selectedFriend.friend.username,
        };
        setMessages((prev) => [...prev, msg]);
        setNewMessage("");
      })
      .catch((err) => handleAuthError(err));
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSendMessage();
  };

  if (!currentUser) return <div>Loading...</div>;

  return (
    <div className="h-screen flex bg-[#f0f2f5]">
      {/* ✅ Sidebar */}
      {(!isMobile || !selectedFriend) && (
        <div className="w-full md:w-1/4 bg-white shadow-lg flex flex-col border-r border-gray-300">
          <div className="p-4 font-bold text-lg border-b bg-[#ededed]">
            {currentUser}
          </div>
          {/* ✅ Search Box */}
<div className="p-2 border-b bg-gray-100">
  <input
    type="text"
    placeholder="Search friends"
    value={searchTerm}
    onChange={(e) => setSearchTerm(e.target.value)}
    className="w-full p-2 border rounded-md text-sm"
  />
</div>

{/* ✅ Friends List */}
<div className="flex-1 overflow-y-auto">
  <h3 className="px-4 py-2 text-gray-600 text-sm font-semibold bg-gray-100">
    Friends
  </h3>
  {filteredFriends.length > 0 ? (
    filteredFriends.map((friend) => (
      <div
        key={friend.id}
        className={`flex items-center gap-3 p-3 cursor-pointer border-b hover:bg-gray-100 ${
          selectedFriend?.id === friend.id ? "bg-gray-200" : ""
        }`}
        onClick={() => handleSelectFriend(friend)}
      >
        <div className="w-10 h-10 bg-gray-400 rounded-full flex items-center justify-center text-white font-bold">
          {friend.friend?.username?.charAt(0).toUpperCase()}
        </div>
        <span className="font-medium">{friend.friend?.username}</span>
      </div>
    ))
  ) : (
    <div className="p-4 text-gray-500">No friends found</div>
  )}
</div>

          {/* ✅ Add button for desktop at bottom-left */}
          {!isMobile && (
            <div className="p-4 border-t flex justify-start">
              <button
                onClick={() => setShowAddFriend(true)}
                className="bg-green-500 text-white px-4 py-2 rounded-full hover:bg-green-600"
              >
                + Add Friend
              </button>
            </div>
          )}
        </div>
      )}

      {/* ✅ Chat Window */}
      {(!isMobile || selectedFriend) && (
        <div className="flex-1 flex flex-col">
          {selectedFriend ? (
            <>
              <div className="p-4 border-b bg-[#ededed] flex items-center">
                {isMobile && (
                  <button
                    className="mr-3 text-blue-500"
                    onClick={() => {
                      setSelectedFriend(null);
                      sessionStorage.removeItem("selectedFriend");
                    }}
                  >
                    ←
                  </button>
                )}
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-gray-400 rounded-full flex items-center justify-center text-white font-bold">
                    {selectedFriend.friend?.username?.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <span className="font-bold block">{selectedFriend.friend?.username}</span>
                    <div className="text-sm text-gray-500">
                      {status?.online
                        ? "Online"
                        : status?.lastSeen
                        ? `Last seen: ${new Date(status.lastSeen).toLocaleString()}`
                        : "Last seen: unknown"}
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex-1 p-4 overflow-y-auto bg-[#e5ddd5]">
                {messages.length > 0 ? (
                  messages.map((msg, index) => (
                    <div
                      key={index}
                      className={`mb-2 px-3 py-2 rounded-lg w-fit max-w-[60%] break-words ${
                        msg.senderUsername?.toLowerCase() === currentUser?.toLowerCase()
                          ? "bg-[#dcf8c6] ml-auto text-right"
                          : "bg-white mr-auto text-left"
                      }`}
                    >
                      <p>{msg.content}</p>
                      <small className="text-gray-500 text-[8px] block text-right">
                        {new Date(msg.timestamp).toLocaleTimeString([], {
                          hour: "2-digit",
                          minute: "2-digit",
                        })}
                      </small>
                    </div>
                  ))
                ) : (
                  <div className="text-gray-500">No messages yet</div>
                )}
                <div ref={messagesEndRef} />
              </div>

              <div className="sticky bottom-0 p-3 bg-white flex items-center">
                <input
                  type="text"
                  placeholder="Type a message"
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  onKeyPress={handleKeyPress}
                  className="flex-1 border rounded-full px-4 py-2 mr-2"
                />
                <button
                  onClick={handleSendMessage}
                  className="bg-blue-500 text-white w-10 h-10 flex items-center justify-center rounded-full hover:bg-blue-600"
                >
                  ➤
                </button>
              </div>
            </>
          ) : (
            <div className="flex-1 flex items-center justify-center text-gray-500">
              Select a friend to start chatting
            </div>
          )}
        </div>
      )}

      {/* ✅ Add Friend Modal */}
      {showAddFriend && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded shadow-lg w-96 max-h-96 overflow-y-auto">
            <h2 className="text-xl font-bold mb-4">Add Friend</h2>
            {allUsers.length > 0 ? (
              allUsers.map((user) => (
                <div
                  key={user.id}
                  className="flex justify-between items-center mb-2"
                >
                  <span>{user.username}</span>
                  <button
                    onClick={() => handleAddFriend(user.id)}
                    className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                  >
                    Add
                  </button>
                </div>
              ))
            ) : (
              <p>No users found</p>
            )}
            <button
              onClick={() => setShowAddFriend(false)}
              className="mt-4 w-full bg-gray-500 text-white py-2 rounded hover:bg-gray-600"
            >
              Close
            </button>
          </div>
        </div>
      )}

      {/* ✅ Floating Add button for mobile */}
      {isMobile && !selectedFriend && (
        <button
          onClick={() => setShowAddFriend(true)}
          className="fixed bottom-5 right-5 bg-green-500 text-white w-14 h-14 rounded-full shadow-lg flex items-center justify-center text-3xl hover:bg-green-600"
        >
          +
        </button>
      )}
    </div>
  );
}
