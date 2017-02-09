package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * net_peerCount
 */
public class AdminPeers extends Response<List<AdminPeers.Peer>>
{

    public List<AdminPeers.Peer> getPeers()
    {
        return getResult();
    }

    public static class Protocol
    {
        private Integer version;
        private Integer difficulty;
        private String  head;

        public Integer getVersion()
        {
            return version;
        }

        public void setVersion(Integer version)
        {
            this.version = version;
        }

        public Integer getDifficulty()
        {
            return difficulty;
        }

        public void setDifficulty(Integer difficulty)
        {
            this.difficulty = difficulty;
        }

        public String getHead()
        {
            return head;
        }

        public void setHead(String head)
        {
            this.head = head;
        }
    }

    public static class Protocols
    {
        private Protocol eth;

        public Protocol getEth()
        {
            return eth;
        }

        public void setEth(Protocol eth)
        {
            this.eth = eth;
        }
    }

    public static class Network
    {
        private String localAddress;
        private String remoteAddress;

        public String getLocalAddress()
        {
            return localAddress;
        }

        public void setLocalAddress(String localAddress)
        {
            this.localAddress = localAddress;
        }

        public String getRemoteAddress()
        {
            return remoteAddress;
        }

        public void setRemoteAddress(String remoteAddress)
        {
            this.remoteAddress = remoteAddress;
        }
    }

    public static class Peer
    {
        private String id;
        private String name;
        private List<String> caps;
        private Network network;
        private Protocols protocols;

        public Peer()
        {
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public List<String> getCaps()
        {
            return caps;
        }

        public void setCaps(List<String> caps)
        {
            this.caps = caps;
        }

        public Network getNetwork()
        {
            return network;
        }

        public void setNetwork(Network network)
        {
            this.network = network;
        }

        public Protocols getProtocols()
        {
            return protocols;
        }

        public void setProtocols(Protocols protocols)
        {
            this.protocols = protocols;
        }
    }

}
