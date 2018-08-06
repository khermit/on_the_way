##　paxos

简单来说：

Phase1：确定谁的编号最高，只有编号最高者才有权利提交proposal；
Phase2：编号最高者提交proposal，如果没有其他节点提出更高编号的proposal，则该提案会被顺利通过；否则，整个过程就会重来。
你编号高，我比你更高，反复如此，算法永远无法结束，这叫活锁。FLP Impossibility已经证明，在异步通信中不存在任何一致性算法，活锁便是Paxos无法解决的硬伤。
Phase1，Phase2非常像2PC中的两个阶段，因此paxos本质上是多个2PC交替执行！