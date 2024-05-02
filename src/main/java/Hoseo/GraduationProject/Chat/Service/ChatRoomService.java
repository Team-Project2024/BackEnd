package Hoseo.GraduationProject.Chat.Service;

import Hoseo.GraduationProject.Chat.DTO.Response.ResponseChatRoomDTO;
import Hoseo.GraduationProject.Chat.Domain.ChatRoom;
import Hoseo.GraduationProject.Chat.ExceptionType.ChatRoomExceptionType;
import Hoseo.GraduationProject.Chat.Repository.ChatBotRepository;
import Hoseo.GraduationProject.Chat.Repository.ChatRoomRepository;
import Hoseo.GraduationProject.Chat.Repository.UserChatRepository;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.Domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserChatRepository userChatRepository;
    private final ChatBotRepository chatBotRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(Member member){
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();

        try{
            chatRoomRepository.save(chatRoom);
        }catch(Exception e){
            throw new BusinessLogicException(ChatRoomExceptionType.SAVE_CHATROOM_ERROR);
        }

        return chatRoom.getId();
    }

    @Transactional(readOnly = true)
    public List<ResponseChatRoomDTO> getChatRoomList(Member member){
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberId(member.getId());
        List<ResponseChatRoomDTO> responseChatRoomDTOList = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms){
            ResponseChatRoomDTO responseChatRoomDTO = new ResponseChatRoomDTO();
            responseChatRoomDTO.setChatRoomId(chatRoom.getId());
            responseChatRoomDTO.setFirstChat(chatRoom.getFirstChat());
            responseChatRoomDTO.setLastChatDate(chatRoom.getLastChatDate());
            responseChatRoomDTOList.add(responseChatRoomDTO);
        }

        return responseChatRoomDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteChatRoom(Member member, Long chatRoomId){
        //채팅방 삭제
        chatBotRepository.deleteByChatRoomId(chatRoomId);
        userChatRepository.deleteByChatRoomId(chatRoomId);
        chatRoomRepository.deleteByIdAndMemberId(chatRoomId, member.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAllChatRoom(Member member){
        chatBotRepository.deleteByMemberId(member.getId());
        userChatRepository.deleteByMemberId(member.getId());
        chatRoomRepository.deleteAllByMemberId(member.getId());
    }
}
