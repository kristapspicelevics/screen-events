import { WebPlugin } from '@capacitor/core';
import type { ScreenEventsPlugin } from './definitions';
export declare class ScreenEventsWeb extends WebPlugin implements ScreenEventsPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    isScreenOn(): Promise<{
        result: boolean;
    }>;
    getUsageStats(options: {
        startTime: number;
        endTime: number;
    }): Promise<{
        result: string;
    }>;
    getUsageEvents(options: {
        startTime: number;
        endTime: number;
    }): Promise<{
        result: string;
    }>;
    checkPermissions(): Promise<{
        result: boolean;
    }>;
    requestPermissions(): Promise<{
        result: boolean;
    }>;
    start(): Promise<{
        result: boolean;
    }>;
    stop(): Promise<{
        result: boolean;
    }>;
    getTotalScreenTime(): Promise<{
        result: string;
    }>;
    resetScreenTime(): Promise<{
        result: string;
    }>;
}
