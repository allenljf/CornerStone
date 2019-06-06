package com.trubuzz.cornerstone.util.download;

import com.trubuzz.cornerstone.util.bus.RxBus;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;

    private BufferedSource bufferedSource;
    private String tag;

    public ProgressResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public ProgressResponseBody(ResponseBody responseBody, String tag) {
        this.responseBody = responseBody;
        this.tag = tag;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                RxBus.getDefault().post(new DownLoadStateBean(contentLength(), bytesReaded, tag));
                return bytesRead;
            }
        };
    }
}
