package com.polaris.controllerTest;

import com.polaris.controller.EntryController;
import com.polaris.dto.EntryDTO;
import com.polaris.dto.HeaderDTO;
import com.polaris.dto.LikeDTO;
import com.polaris.dto.converter.EntryDTOConverter;
import com.polaris.dto.converter.HeaderDTOConverter;
import com.polaris.dto.converter.LikeDTOConverter;
import com.polaris.entity.Entry;
import com.polaris.entity.Header;
import com.polaris.entity.Like;
import com.polaris.service.impl.EntryServiceImpl;
import com.polaris.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EntryControllerTest {

    @InjectMocks
    EntryController entryController;

    @Mock
    private EntryServiceImpl entryService;

    @Mock
    private EntryDTOConverter entryDTOConverter;

    @Mock
    private HeaderDTOConverter headerDTOConverter;

    @Mock
    private LikeDTOConverter likeDTOConverter;

    @Mock
    private LikeServiceImpl likeService;

    List<Entry> lastEntries = new ArrayList<>();
    List<EntryDTO> lastEntriesDTO = new ArrayList<>();

    @BeforeEach
    void setLastEntries() {
        for(int i = 1; i<5; i++) {
            Entry entry = new Entry(i, "Bu guzel bir entry",null,null,null,null);
            lastEntries.add(entry);
        }
        for(int i = 1; i<5; i++) {
            EntryDTO entryDTO = new EntryDTO(i, null,null,null,"Bu guzel bir entry",null);
            lastEntriesDTO.add(entryDTO);
        }
    }

    @Test
    void shouldDeleteEntryByIdSuccessfully() {
        EntryDTO entryDTO = new EntryDTO(1, null, null, null, "Bu guzel bir entry", null);
        Entry entry = new Entry(1, "Bu guzel bir entry",null,null,null,null);
        Mockito.when(entryService.getEntryById(ArgumentMatchers.any(Integer.class))).thenReturn(entry);
        Mockito.when(entryDTOConverter.getEntryDTOFromEntry(ArgumentMatchers.any(Entry.class))).thenReturn(entryDTO);
        Mockito.doAnswer(invocation -> {
            entry.setId(null);
            entry.setContent(null);
            return null;
        }).when(entryService).deleteEntryById(Mockito.any(Integer.class));
        ResponseEntity<EntryDTO> responseEntity = entryController.deleteEntry(1);
        Assertions.assertEquals(entry.getId(), null);
        Assertions.assertEquals(responseEntity.getBody().getContent(), entryDTO.getContent());
    }

    @Test
    void shouldUpdateEntrySuccessfully() {
        EntryDTO entryDTO = new EntryDTO(1, null, null, null, "Bu guzel bir entry mi", null);
        Entry entry = new Entry(1, "Bu guzel bir entry",null,null,null,null);
        Mockito.when(entryDTOConverter.getEntryFromEntryDTO(ArgumentMatchers.any(EntryDTO.class))).thenReturn(entry);
        Mockito.doAnswer(invocation -> {
            entry.setContent(entryDTO.getContent());
            return null;
        }).when(entryService).updateEntry(Mockito.any(Entry.class));
        ResponseEntity<EntryDTO> responseEntity = entryController.updateEntry(entryDTO,1);
        Assertions.assertEquals(entry.getContent(), entryDTO.getContent());
        Assertions.assertEquals(responseEntity.getBody().getContent(), entryDTO.getContent());
    }

    @Test
    void shouldReturnAllHeadersByHeader() {
        Header header = new Header(1, "header", null);
        HeaderDTO headerDTO = new HeaderDTO(1, "header", null);
        header.setEntries(lastEntries);
        headerDTO.setEntries(lastEntriesDTO);
        Mockito.when(headerDTOConverter.getHeaderFromHeaderDTO(ArgumentMatchers.any(HeaderDTO.class))).thenReturn(header);
        Mockito.when(entryService.findEntriesByHeader(ArgumentMatchers.any(Header.class))).thenReturn(lastEntries);
        Mockito.when(entryDTOConverter.getListOfEntryDTOFromListOfEntry(ArgumentMatchers.anyList())).thenReturn(lastEntriesDTO);
        ResponseEntity<List<EntryDTO>> responseEntity = entryController.getAllEntriesOfTheHeader(headerDTO);
        Assertions.assertEquals(responseEntity.getBody().get(1).getContent(), new Entry(1, "Bu guzel bir entry",null,null,null,null).getContent());
    }

    @Test
    void shouldReturnEntryByIdSuccessfully() {
        EntryDTO entryDTO = new EntryDTO(1, null, null, null, "Bu guzel bir entry mi", null);
        Entry entry = new Entry(1, "Bu guzel bir entry",null,null,null,null);
        Mockito.when(entryService.getEntryById(ArgumentMatchers.any(Integer.class))).thenReturn(entry);
        Mockito.when(entryDTOConverter.getEntryDTOFromEntry(ArgumentMatchers.any(Entry.class))).thenReturn(entryDTO);
        ResponseEntity<EntryDTO> responseEntity = entryController.getEntry(1);
        Assertions.assertEquals(responseEntity.getBody().getContent(), entryDTO.getContent());
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void shouldReturnLastTenEntriesSuccessfully() {
        Mockito.when(entryService.getLastTenEntries()).thenReturn(lastEntries);
        Mockito.when(entryDTOConverter.getListOfEntryDTOFromListOfEntry(ArgumentMatchers.anyList())).thenReturn(lastEntriesDTO);
        ResponseEntity<List<EntryDTO>> responseEntity = entryController.getLastTenEntry();
        Assertions.assertEquals(responseEntity.getBody().get(1).getContent(), lastEntriesDTO.get(1).getContent());
    }

    @Test
    void shouldReturnTopEntriesByLikeCount() {
        Mockito.when(entryService.getTopTenEntryWithMostLikes()).thenReturn(lastEntries);
        Mockito.when(entryDTOConverter.getListOfEntryDTOFromListOfEntry(ArgumentMatchers.anyList())).thenReturn(lastEntriesDTO);
        ResponseEntity<List<EntryDTO>> responseEntity = entryController.getTopTenEntryAccordingToLike();
        Assertions.assertEquals(responseEntity.getBody().get(1).getContent(), lastEntriesDTO.get(1).getContent());
    }

    @Test
    void shouldLikeEntrySuccessfully() {
        EntryDTO entryDTO = new EntryDTO(1, null, null, null, "Bu guzel bir entry mi", null);
        Entry entry = new Entry(1, "Bu guzel bir entry", null, null, null, null);
        List<Like> list = new ArrayList<>();
        entry.setLikes(list);
        List<LikeDTO> listDTO = new ArrayList<>();
        LikeDTO likeDTO = new LikeDTO(1, null, entryDTO);
        Like like = new Like(1, null, null);
        Mockito.doAnswer(invocation -> {
            list.add(like);
            return null;
        }).when(entryService).updateEntry(Mockito.any(Entry.class));
        Mockito.doAnswer(invocation -> {
            list.add(like);
            return null;
        }).when(likeService).updateLike(Mockito.any(Like.class));
        Mockito.when(likeDTOConverter.getLikeFromLikeDTO(ArgumentMatchers.any(LikeDTO.class))).thenReturn(like);
        Mockito.when(entryService.getEntryById(ArgumentMatchers.any(Integer.class))).thenReturn(entry);
        ResponseEntity<LikeDTO> responseEntity = entryController.likeEntry(1, likeDTO);
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(entryDTO.getContent(), likeDTO.getEntryDTO().getContent());
    }
}

