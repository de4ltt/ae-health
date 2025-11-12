package feo.health.catalog_service.service;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Empty;
import feo.health.catalog_service.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;
import user.UserServiceGrpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserServiceGrpc.UserServiceFutureStub stub;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveToHistory_success() throws ExecutionException, InterruptedException {
        User.SaveToHistoryRequest request = User.SaveToHistoryRequest.newBuilder()
                .setUserId(1)
                .setItemId(2)
                .setItemType("doctor")
                .build();

        ListenableFuture<Empty> listenableFuture = mock(ListenableFuture.class);
        when(stub.saveToHistory(any())).thenReturn(listenableFuture);

        CompletableFuture<Void> future = userService.saveToHistory(request);
        assertDoesNotThrow(() -> future.get());

        verify(stub).saveToHistory(request);
    }
}