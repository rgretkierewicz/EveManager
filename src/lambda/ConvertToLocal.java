package lambda;

import java.time.LocalDateTime;

/**
 * This is the functional interface for the lambda
 *  expression that converts UTC time to local time.
 *  The lambda expression is located in the AppointmentsQuery file.
 */
public interface ConvertToLocal {
    LocalDateTime utcToLocal(LocalDateTime utcTime);
}
