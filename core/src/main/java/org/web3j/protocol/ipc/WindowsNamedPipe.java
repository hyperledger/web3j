/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.ipc;

import java.io.IOException;
import java.io.RandomAccessFile;

/** Windows named pipe IO implementation for IPC. */
public class WindowsNamedPipe implements IOFacade {

    private final RandomAccessFile pipe;

    public WindowsNamedPipe(String ipcSocketPath) {
        try {
            pipe = new RandomAccessFile(ipcSocketPath, "rw");
        } catch (IOException e) {
            throw new RuntimeException("Provided file pipe cannot be opened: " + ipcSocketPath, e);
        }
    }

    @Override
    public void write(String payload) throws IOException {
        pipe.write(payload.getBytes());
    }

    @Override
    public String read() throws IOException {
        return pipe.readLine();
    }

    @Override
    public void close() throws IOException {
        pipe.close();
    }
}
