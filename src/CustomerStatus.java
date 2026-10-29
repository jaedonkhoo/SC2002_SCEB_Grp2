public enum CustomerStatus {
    WAITING,
    SEATED,
    // Marks service during this turn so eating cannot also finish at this turn's end.
    JUST_SERVED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT
}
