import React, { useEffect, useState } from "react";
import api from "../api";
import MessageInput from "./MessageInput";

export default function ChatWindow({ friend }) {
  const [messages, setMessages] = useState([]);

  const fetchMessages = () => {
    api.get(`/message/showchat/${friend.id}`)
      .then((res) => setMessages(res.data))
      .catch((err) => console.error(err));
  };

  useEffect(() => {
    fetchMessages();
    const interval = setInterval(fetchMessages, 3000);
    return () => clearInterval(interval);
  }, [friend.id]);

  const sendMessage = (text) => {
    api.post(`/message/sendto/${friend.id}`, text, {
      headers: { "Content-Type": "text/plain" },
    }).then(fetchMessages);
  };

  return (
    <div className="flex-1 flex flex-col">
      <div className="p-4 border-b font-bold">{friend.username}</div>
      <div className="flex-1 overflow-y-auto p-4 space-y-2">
        {messages.map((msg, i) => (
        <div
  key={i}
  className="bg-gray-200 px-2 py-1 rounded-lg max-w-[60%]"
>
 <p style={{ fontSize: "12px", lineHeight: "14px", fontWeight: "normal" }}>
  {msg.content}
</p>
<span
  style={{ fontSize: "10px", display: "block", textAlign: "right" }}
  className="text-gray-500"
>
  {msg.timestamp}
</span>

</div>
        ))}
      </div>
      <MessageInput onSend={sendMessage} />
    </div>
  );
}
